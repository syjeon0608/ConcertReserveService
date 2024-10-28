package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ApplicationMapper;
import com.hhpl.concertreserve.application.model.payment.PaymentInfo;
import com.hhpl.concertreserve.domain.concert.ConcertService;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.payment.PaymentService;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.user.UserService;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final WaitingQueueService waitingQueueService;

    @Transactional
    public PaymentInfo processPayment(Long reservationId, Long userId, String uuid) {
        concertService.validateSeatStatusForPayment(reservationId);
        Reservation reservation = concertService.convertReservationToComplete(reservationId);
        userService.updateUserPoint(userId, reservation.getTotalAmount(), PointStatus.USE);
        Payment payment = paymentService.completePayment(userId, reservation, reservation.getTotalAmount());
        waitingQueueService.expireQueueOnPaymentCompletion(uuid);
        return ApplicationMapper.PaymentMapper.from(payment);
    }

}
