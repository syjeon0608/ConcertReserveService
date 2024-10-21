package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.PaymentMapperApplication;
import com.hhpl.concertreserve.application.model.PaymentInfo;
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
    private final PaymentMapperApplication paymentMapper;

    @Transactional
    public PaymentInfo processPayment(Long reservationId, Long userId, String uuid) {
        Reservation reservation = concertService.getReservationInfo(reservationId);
        concertService.completeReservationByPayment(reservationId);
        userService.updateUserPoint(userId, reservation.getTotalAmount(), PointStatus.USE);
        waitingQueueService.expireQueueOnPaymentCompletion(uuid);
        Payment payment = paymentService.completePayment(userId, reservation, reservation.getTotalAmount());
        return paymentMapper.from(payment);
    }

}
