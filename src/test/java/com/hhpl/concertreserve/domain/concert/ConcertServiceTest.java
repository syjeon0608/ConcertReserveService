package com.hhpl.concertreserve.domain.concert;

import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("콘서트는 예매오픈일자 ~ 콘서트 종료일자까지 리스트에서 조회된다.")
    public void testGetAvailableConcerts() {
        LocalDateTime fixedNow = LocalDateTime.of(2024, 10, 5, 12, 0, 0);
        List<Concert> mockConcerts = new ArrayList<>();
        //조회o- 예매오픈일 ~ 콘서트 종료일
        mockConcerts.add(new Concert(1L, "Concert A", "Description A",
                LocalDateTime.of(2024, 10, 3, 12, 0, 0),    // openDate
                LocalDateTime.of(2024, 10, 10, 12, 0, 0),   // startDate
                LocalDateTime.of(2024, 10, 11, 12, 0, 0)));  // endDate
        //조회x - 예매오픈일이 도래하지않음
        mockConcerts.add(new Concert(2L, "Concert B", "Description B",
                LocalDateTime.of(2024, 10, 8, 12, 0, 0),    // openDate
                LocalDateTime.of(2024, 10, 10, 12, 0, 0),   // startDate
                LocalDateTime.of(2024, 10, 11, 12, 0, 0)));  // endDate
        //조회x - 종료된 콘서트
        mockConcerts.add(new Concert(3L, "Concert C", "Description C",
                LocalDateTime.of(2024, 10, 1, 12, 0, 0),    // openDate
                LocalDateTime.of(2024, 10, 2, 12, 0, 0),    // startDate
                LocalDateTime.of(2024, 10, 4, 12, 0, 0)));  // endDate

        when(concertRepository.getAvailableConcerts(any(LocalDateTime.class))).thenAnswer(invocation -> {
            return mockConcerts.stream()
                    .filter(concert -> concert.getOpenDate().isBefore(fixedNow) || concert.getOpenDate().isEqual(fixedNow))
                    .filter(concert -> concert.getEndDate().isAfter(fixedNow) || concert.getEndDate().isEqual(fixedNow))
                    .collect(Collectors.toList());
        });

        List<Concert> availableConcerts = concertService.getAvailableConcerts();

        assertEquals(1, availableConcerts.size());
        assertEquals("Concert A", availableConcerts.get(0).getTitle());
    }


    @Test
    @DisplayName("콘서트의 예약가능한 스케쥴을 조회한다.- 잔여좌석이 0 이면 조회불가")
    public void testGetAvailableSchedules() {
        Long concertId = 1L;
        Concert mockConcert = new Concert(concertId, "Concert A", "Description A",
                LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        List<Schedule> mockSchedules = Arrays.asList(
                new Schedule(1L, mockConcert, LocalDateTime.now(), 10, 50),
                new Schedule(2L, mockConcert, LocalDateTime.now(), 0, 50)
        );

        when(concertRepository.getSchedulesWithAvailableSeats(concertId, 0))
                .thenReturn(mockSchedules.stream()
                        .filter(schedule -> schedule.getAvailableSeats() > 0)
                        .collect(Collectors.toList()));

        List<Schedule> availableSchedules = concertService.getAvailableSchedules(concertId);

        assertEquals(1, availableSchedules.size());
        assertEquals(10, availableSchedules.get(0).getAvailableSeats());
        assertEquals(50, availableSchedules.get(0).getTotalSeats());
    }

    @Test
    @DisplayName("스케쥴의 예약가능한 좌석을 조회한다.-UNAVAILABLE 조회불가")
    public void testGetAvailableSeats() {
        Long scheduleId = 1L;
        Concert mockConcert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule mockSchedule = new Schedule(scheduleId, mockConcert, LocalDateTime.now(), 50, 100);
        List<Seat> mockSeats = Arrays.asList(
                new Seat(1L,  mockSchedule, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusHours(1),0L),
                new Seat(2L,  mockSchedule, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusHours(1),0L),
                new Seat(3L,  mockSchedule, 50000, SeatStatus.UNAVAILABLE, LocalDateTime.now().plusHours(1),0L)
        );
        when(concertRepository.getAvailableSeats(scheduleId, SeatStatus.AVAILABLE))
                .thenReturn(mockSeats.stream()
                        .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                        .collect(Collectors.toList()));

        List<Seat> availableSeats = concertService.getAvailableSeats(scheduleId);

        assertEquals(2, availableSeats.size());
        assertEquals(SeatStatus.AVAILABLE, availableSeats.get(0).getStatus());
    }

}