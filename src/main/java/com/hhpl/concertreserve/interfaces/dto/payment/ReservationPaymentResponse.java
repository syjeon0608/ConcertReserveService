package com.hhpl.concertreserve.interfaces.dto.payment;

import java.time.LocalDateTime;

public record ReservationPaymentResponse(
        Long tokenId,
        Long reservationId,
        Long concertId,
        Long scheduleId,
        int seatNumber,
        String seatStatus,
        String reservationStatus,
        LocalDateTime reservedAt,
        LocalDateTime updatedAt,
        Long amount
) {
}
