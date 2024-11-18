package com.hhpl.concertreserve.app.payment.api.listener;

import com.hhpl.concertreserve.app.payment.domain.event.PaymentSuccessEvent;
import com.hhpl.concertreserve.app.waitingqueue.domain.service.WaitingQueueCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessTokenExpireListener {

    private final WaitingQueueCacheService waitingQueueCacheService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void expireTokenAfterPayment(PaymentSuccessEvent event) {
        try {
            waitingQueueCacheService.expireTokenAfterPayment(event.token());
            log.info("[SUCCESS] Token expired successfully for token: {}" , event.token());
        } catch (Exception e) {
            log.error("[FAIL] expire token: {}", event.token(), e);
        }
    }
}