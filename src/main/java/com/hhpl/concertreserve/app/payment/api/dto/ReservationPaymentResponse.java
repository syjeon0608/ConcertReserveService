package com.hhpl.concertreserve.app.payment.api.dto;

import java.time.LocalDateTime;

public record ReservationPaymentResponse(
        Long reservationId,
        Long userId,
        Long concertId,
        int amount,
        String reservationStatus,
        LocalDateTime paymentDate
) {
}
