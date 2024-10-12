package com.hhpl.concertreserve.interfaces.dto.concert;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long tokenId,
        Long concertId,
        Long scheduleId,
        int seatNumber,
        String seatStatus,
        String reservationStatus,
        LocalDateTime reservedAt
) {
}