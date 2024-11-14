package com.hhpl.concertreserve.web.application;

import com.hhpl.concertreserve.app.ConcertReservationServiceApplication;
import com.hhpl.concertreserve.app.concert.application.ConcertFacade;
import com.hhpl.concertreserve.app.concert.domain.*;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.concert.infra.database.ConcertJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.ScheduleJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.SeatJpaRepository;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import com.hhpl.concertreserve.app.user.infra.database.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.hhpl.concertreserve.app.concert.domain.SeatStatus.UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConcertReservationServiceApplication.class)
@Transactional
public class ConcertFacadeIntegrationTest {

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private Concert testConcert;
    private Schedule testSchedule;
    private Seat testSeat;
    private User testUser;

    @BeforeEach
    void setUp() {
        testConcert = concertJpaRepository.save(new Concert(null, "Test Concert", "Description", LocalDateTime.now().minusDays(1), LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
        testSchedule = scheduleJpaRepository.save(new Schedule(null, testConcert, LocalDateTime.now().plusDays(1), 100, 100));
        testSeat = seatJpaRepository.save(new Seat(null, testSchedule, 100, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),0L));

    }

    @AfterEach
    void cleanUp() {
        seatJpaRepository.deleteAll();
        scheduleJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();
    }


    @Test
    @DisplayName("성공적으로 사용 가능한 스케줄 조회")
    void shouldGetAvailableSchedules() {
        List<ScheduleInfo> availableSchedules = concertFacade.getAvailableSchedules(testConcert.getId());

        assertEquals(1, availableSchedules.size());
        assertEquals(testSchedule.getId(), availableSchedules.get(0).id());
    }

    @Test
    @DisplayName("성공적으로 사용 가능한 좌석 조회")
    void shouldGetAvailableSeats() {
        List<SeatInfo> availableSeats = concertFacade.getAvailableSeats(testSchedule.getId());

        assertEquals(1, availableSeats.size());
        assertEquals(testSeat.getId(), availableSeats.get(0).id());
    }

    @Test
    @DirtiesContext
    @DisplayName("성공적으로 임시 예약 생성")
    void shouldCreateTemporarySeatReservation() {
        testUser = userJpaRepository.save(new User(null,"uuid-1"));
        ReservationInfo reservation = concertFacade.createTemporarySeatReservation(testUser.getUuid(), testSeat.getId());

        assertEquals(testSeat.getId(), reservation.seatId());
        assertEquals(UNAVAILABLE, testSeat.getStatus());
        assertEquals(ReservationStatus.TEMPORARY, reservation.status());
    }

}
