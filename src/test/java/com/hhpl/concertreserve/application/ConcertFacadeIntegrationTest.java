package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.application.facade.ConcertFacade;
import com.hhpl.concertreserve.application.model.ConcertInfo;
import com.hhpl.concertreserve.application.model.ReservationInfo;
import com.hhpl.concertreserve.application.model.ScheduleInfo;
import com.hhpl.concertreserve.application.model.SeatInfo;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.ReservationStatus;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.infra.concert.ConcertJpaRepository;
import com.hhpl.concertreserve.infra.concert.ScheduleJpaRepository;
import com.hhpl.concertreserve.infra.concert.SeatJpaRepository;
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

import static com.hhpl.concertreserve.domain.concert.type.SeatStatus.UNAVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
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

    private Concert testConcert;
    private Schedule testSchedule;
    private Seat testSeat;

    @BeforeEach
    void setUp() {
        testConcert = concertJpaRepository.save(new Concert(null, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        testSchedule = scheduleJpaRepository.save(new Schedule(null, testConcert, LocalDateTime.now().plusDays(1), 100, 100));
        testSeat = seatJpaRepository.save(new Seat(null, testSchedule, 100, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1)));

    }

    @AfterEach
    void cleanUp() {
        seatJpaRepository.deleteAll();
        scheduleJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("성공적으로 사용 가능한 콘서트 조회")
    void shouldGetAvailableConcerts() {
        List<ConcertInfo> availableConcerts = concertFacade.getAvailableConcerts();

        assertEquals(1, availableConcerts.size());
        assertEquals("Test Concert", availableConcerts.get(0).title());
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
        ReservationInfo reservation = concertFacade.createTemporarySeatReservation("550e8400-e29b-41d4-a716-446655440000", testSeat.getId());

        assertEquals(testSeat.getId(), reservation.seatId());
        assertEquals(UNAVAILABLE, testSeat.getStatus());
        assertEquals(ReservationStatus.TEMPORARY, reservation.status());
    }

}
