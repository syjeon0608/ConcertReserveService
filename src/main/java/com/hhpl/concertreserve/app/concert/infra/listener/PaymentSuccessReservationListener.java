package com.hhpl.concertreserve.app.concert.infra.listener;

import com.hhpl.concertreserve.app.concert.infra.externalapi.DataPlatformApiClient;
import com.hhpl.concertreserve.app.payment.domain.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentSuccessReservationListener {

    private final DataPlatformApiClient dataPlatformApiClient;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendReservationInfoAfterPayment(PaymentSuccessEvent event) {
        dataPlatformApiClient.sendReservationInfo(event.getReservationInfo());
    }
}