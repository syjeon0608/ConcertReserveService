package com.hhpl.concertreserve.app.payment.domain;

import java.time.LocalDateTime;

public record PaymentInfo (
        Long reservationId,
        Long userId,
        Long seatId,
        int amount,
        String reservationStatus,
        LocalDateTime paymentDate
){
}
