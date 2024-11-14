package com.hhpl.concertreserve.app.payment.application;

import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import com.hhpl.concertreserve.app.payment.domain.PaymentSuccessEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PaymentSuccessEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public PaymentSuccessEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void success(ReservationInfo reservationInfo, String token) {
        eventPublisher.publishEvent(new PaymentSuccessEvent(this, reservationInfo, token));
    }

}
