package com.hhpl.concertreserve.app.payment.infra.event;

import com.hhpl.concertreserve.app.payment.domain.event.PaymentSuccessEventPublisher;
import com.hhpl.concertreserve.app.payment.domain.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSuccessEventPublisherImpl implements PaymentSuccessEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void success(Long reservationId, Long paymentId, String token) {
        eventPublisher.publishEvent(new PaymentSuccessEvent(reservationId, paymentId ,token));
    }
}
