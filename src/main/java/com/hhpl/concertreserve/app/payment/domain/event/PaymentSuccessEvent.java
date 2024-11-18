package com.hhpl.concertreserve.app.payment.domain.event;

public record PaymentSuccessEvent(
        Long reservationId,
        Long paymentId,
        String token
) { }