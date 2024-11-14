package com.hhpl.concertreserve.web.domain.concert;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.concert.domain.ReservationStatus;
import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.app.common.error.ErrorType.RESERVATION_ALREADY_CANCELED;
import static com.hhpl.concertreserve.app.common.error.ErrorType.RESERVATION_ALREADY_COMPLETED;
import static com.hhpl.concertreserve.app.concert.domain.ReservationStatus.CANCEL;
import static com.hhpl.concertreserve.app.concert.domain.ReservationStatus.COMPLETE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTest {

    @Test
    @DisplayName("예약신청 시 예약상태를 TEMPORARY로 설정한다.")
    void shouldSetReservationStatusToTemporary() {
        User user = new User(1L,"uuid1");
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now(),0L);

        Reservation reservation = new Reservation(user, seat);

        assertEquals(ReservationStatus.TEMPORARY, reservation.getReservationStatus());
        assertEquals(80000, reservation.getTotalAmount());
    }

    @Test
    @DisplayName("예약 상태를 TEMPORARY에서 COMPLETE으로 변경한다")
    void shouldUpdateReservationStatusToComplete() {
        User user = new User(1L,"uuid1");
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now(),0L);
        Reservation reservation = new Reservation(user, seat);

        reservation.completeReservation();

        assertEquals(COMPLETE, reservation.getReservationStatus());
    }

    @Test
    @DisplayName("예약 상태를 TEMPORARY에서 CANCEL로 변경한다")
    void shouldUpdateReservationStatusToCancel() {
        User user = new User(1L,"uuid1");
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now(),0L);
        Reservation reservation = new Reservation(user, seat);

        reservation.cancelReservation();

        assertEquals(CANCEL, reservation.getReservationStatus());

    }

    @Test
    @DisplayName("이미 취소된 예약에 대해 다시 취소 요청이 들어오면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenCancelAlreadyCancelledReservation() {
        User user = new User(1L,"uuid1");
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now(),0L);

        Reservation reservation = new Reservation(user, seat);
        reservation.cancelReservation();
        assertEquals(CANCEL,reservation.getReservationStatus());

        CoreException exception = assertThrows(CoreException.class, reservation::cancelReservation);
        assertEquals(RESERVATION_ALREADY_CANCELED,exception.getErrorType());
    }

    @Test
    @DisplayName("이미 완료된 예약에 대해 다시 완료 요청이 들어오면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenCompleteAlreadyCompletedReservation() {
        User user = new User(1L,"uuid1");
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now(),0L);

        Reservation reservation = new Reservation(user, seat);
        reservation.completeReservation();
        assertEquals(COMPLETE,reservation.getReservationStatus());

        CoreException exception = assertThrows(CoreException.class, reservation::completeReservation);
        assertEquals(RESERVATION_ALREADY_COMPLETED,exception.getErrorType());
    }

}