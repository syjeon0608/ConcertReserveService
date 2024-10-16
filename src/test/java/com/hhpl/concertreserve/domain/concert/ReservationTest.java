package com.hhpl.concertreserve.domain.concert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.ReservationStatus.CANCEL;
import static com.hhpl.concertreserve.domain.concert.ReservationStatus.COMPLETE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest {

    @Test
    @DisplayName("예약신청 시 예약상태를 TEMPORARY로 설정한다.")
    void shouldSetReservationStatusToTemporary() {
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now());

        Reservation reservation = new Reservation("uuid", seat);

        assertEquals(ReservationStatus.TEMPORARY, reservation.getReservationStatus());
        assertEquals(80000, reservation.getTotalAmount());
    }

    @Test
    @DisplayName("예약 상태를 TEMPORARY에서 COMPLETE으로 변경한다")
    void shouldUpdateReservationStatusToComplete() {
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now());
        Reservation reservation = new Reservation("uuid", seat);

        reservation.makeStatusComplete();

        assertEquals(COMPLETE, reservation.getReservationStatus());
    }

    @Test
    @DisplayName("예약 상태를 TEMPORARY에서 CANCEL로 변경한다")
    void shouldUpdateReservationStatusToCancel() {
        Concert concert = new Concert(1L, "Concert A", "Description A", LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Schedule schedule = new Schedule(1L, concert, LocalDateTime.now(), 50, 100);
        Seat seat = new Seat(1L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now());
        Reservation reservation = new Reservation("uuid", seat);

        reservation.makeStatusCancel();

        assertEquals(CANCEL, reservation.getReservationStatus());

    }
}