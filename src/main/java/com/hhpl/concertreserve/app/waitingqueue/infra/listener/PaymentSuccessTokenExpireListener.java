package com.hhpl.concertreserve.app.waitingqueue.infra.listener;

import com.hhpl.concertreserve.app.payment.domain.PaymentSuccessEvent;
import com.hhpl.concertreserve.app.waitingqueue.domain.service.WaitingQueueCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentSuccessTokenExpireListener {

    private final WaitingQueueCacheService waitingQueueCacheService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void expireTokenAfterPayment(PaymentSuccessEvent event) {
        waitingQueueCacheService.expireTokenAfterPayment(event.getToken());
    }
}