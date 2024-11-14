package com.hhpl.concertreserve.app.payment.api.dto;

public record ReservationPaymentRequest(
        Long userId,
        Long reservationId
) {
}