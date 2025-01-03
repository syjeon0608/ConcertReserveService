package com.hhpl.concertreserve.web.domain.concert;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.app.common.error.ErrorType.*;
import static com.hhpl.concertreserve.app.concert.domain.SeatStatus.AVAILABLE;
import static com.hhpl.concertreserve.app.concert.domain.SeatStatus.UNAVAILABLE;
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
        Seat seat = new Seat(1L, schedule, 80000, AVAILABLE, reservedTime,0L);

        seat.inactive();

        assertEquals(UNAVAILABLE, seat.getStatus());
        assertEquals(reservedTime.plusMinutes(5), seat.getExpiredAt());
    }

    @Test
    @DisplayName("좌석이 만료되면 좌석상태는 UNAVAILABLE에서 AVAILABLE로 변경된다")
    void shouldChangeSeatStatusToAvailableWhenExpired() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.UNAVAILABLE, expiredTime,0L);

        seat.reactivate();

        assertEquals(AVAILABLE, seat.getStatus());
    }

    @Test
    @DisplayName("좌석이 만료되면 예외를 던진다")
    void shouldThrowExceptionWhenSeatIsExpired() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        Seat seat = new Seat(1L, schedule, 80000, UNAVAILABLE, expiredTime,0L);

        CoreException exception = assertThrows(CoreException.class, seat::validateForSeatExpired);
        assertEquals(SEAT_IS_EXPIRED, exception.getErrorType());
    }

    @Test
    @DisplayName("예약 불가능한 좌석에 대해 예약을 시도하면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenReservingUnavailableSeat() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        Seat seat = new Seat(1L, schedule, 80000, AVAILABLE, expiredTime,0L);
        seat.inactive();
        assertEquals(UNAVAILABLE, seat.getStatus());

        CoreException exception = assertThrows(CoreException.class, seat::inactive);
        assertEquals(SEAT_ALREADY_UNAVAILABLE, exception.getErrorType());

    }

    @Test
    @DisplayName("이미 예약 가능한 좌석에 대해 재활성화 요청을 하면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenReactivatingAlreadyAvailableSeat() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        Seat seat = new Seat(1L, schedule, 80000, UNAVAILABLE, expiredTime,0L);
        seat.reactivate();
        assertEquals(AVAILABLE, seat.getStatus());

        CoreException exception = assertThrows(CoreException.class, seat::reactivate);
        assertEquals(SEAT_ALREADY_AVAILABLE, exception.getErrorType());
    }

}