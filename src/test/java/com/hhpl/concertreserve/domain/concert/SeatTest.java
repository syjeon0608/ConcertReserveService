package com.hhpl.concertreserve.domain.concert;

import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.domain.error.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.type.SeatStatus.AVAILABLE;
import static com.hhpl.concertreserve.domain.concert.type.SeatStatus.UNAVAILABLE;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.SEAT_IS_EXPIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class SeatTest {

    Concert concert = new Concert();
    Schedule schedule = new Schedule();

    @BeforeEach
    public void setUp() {
        concert = new Concert(1L, "title", "description", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());
        schedule = new Schedule(1L, concert, LocalDateTime.now(), 30, 50);
    }

    @Test
    @DisplayName("예약 시 좌석을 AVAILABLE에서 UNAVILABLE로 변경하고, 좌석 만료시간을 5분 뒤로 설정한다.")
    void shouldChangeSeatStatusToUnavailableWhenReserved() {
        LocalDateTime reservedTime = LocalDateTime.of(2024, 10, 15, 12, 0);

        mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(reservedTime);
        Seat seat = new Seat(1L, schedule, 80000, AVAILABLE, reservedTime);

        seat.inactive();

        assertEquals(UNAVAILABLE, seat.getStatus());
        assertEquals(reservedTime.plusMinutes(5), seat.getExpiredAt());
    }

    @Test
    @DisplayName("좌석이 만료되면 좌석상태는 UNAVAILABLE에서 AVAILABLE로 변경된다")
    void shouldChangeSeatStatusToAvailableWhenExpired() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.UNAVAILABLE, expiredTime);

        seat.reactivate();

        assertEquals(AVAILABLE, seat.getStatus());
    }

    @Test
    @DisplayName("좌석이 만료되면 예외를 던진다")
    void shouldThrowExceptionWhenSeatIsExpired() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        Seat seat = new Seat(1L, schedule, 80000, UNAVAILABLE, expiredTime);

        BusinessException exception = assertThrows(BusinessException.class, seat::validate);
        assertEquals(SEAT_IS_EXPIRED, exception.getErrorCode());
    }
}