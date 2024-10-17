package com.hhpl.concertreserve.interfaces.dto.payment;

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
