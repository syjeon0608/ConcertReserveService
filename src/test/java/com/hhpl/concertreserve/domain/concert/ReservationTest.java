package com.hhpl.concertreserve.domain.concert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationTest {

    @Test
    @DisplayName("예약신청 시 예약상태를 TEMP로 설정한다.")
    void shouldSetReservationStatusToTemporary() {
        Reservation reservation = new Reservation("uuid", 1L);
        assertEquals(ReservationStatus.TEMPORARY, reservation.getReservationStatus());
    }

}