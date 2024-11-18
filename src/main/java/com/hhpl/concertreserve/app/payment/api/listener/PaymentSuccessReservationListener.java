package com.hhpl.concertreserve.app.payment.api.listener;

import com.hhpl.concertreserve.app.payment.domain.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessReservationListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReservationInfoAfterPayment(PaymentSuccessEvent event) {
        try {
            sendReservationInfo(event.reservationId(),event.paymentId());
        } catch (Exception e) {
            log.error("[FAIL] send reservation info: {}", event, e);
        }
    }

    private void sendReservationInfo(Long reservationId, Long paymentId) {
        log.info("[SUCCESS] reservationId: {}, paymentId: {}", reservationId, paymentId);
    }
}