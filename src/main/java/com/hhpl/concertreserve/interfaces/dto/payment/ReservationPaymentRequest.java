package com.hhpl.concertreserve.interfaces.dto.payment;

public record ReservationPaymentRequest(
        Long userId,
        Long concertId,
        int amount
) {
}