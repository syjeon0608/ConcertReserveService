package com.hhpl.concertreserve.app.payment.domain.event;

public interface PaymentSuccessEventPublisher {
    void success(Long reservationId, Long paymentId, String token);
}
