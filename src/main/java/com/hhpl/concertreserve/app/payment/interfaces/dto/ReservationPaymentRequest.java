package com.hhpl.concertreserve.app.payment.interfaces.dto;

public record ReservationPaymentRequest(
        Long userId,
        Long reservationId
) {
}