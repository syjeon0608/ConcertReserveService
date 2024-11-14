package com.hhpl.concertreserve.app.payment.application;

import com.hhpl.concertreserve.app.common.mapper.ApplicationMapper;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.service.ConcertService;
import com.hhpl.concertreserve.app.payment.domain.PaymentInfo;
import com.hhpl.concertreserve.app.payment.domain.entity.Payment;
import com.hhpl.concertreserve.app.payment.domain.service.PaymentService;
import com.hhpl.concertreserve.app.user.domain.PointStatus;
import com.hhpl.concertreserve.app.user.domain.service.UserService;
import com.hhpl.concertreserve.app.waitingqueue.domain.service.WaitingQueueCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final WaitingQueueCacheService waitingQueueCacheService;

    @Transactional
    public PaymentInfo processPayment(Long reservationId, Long userId, String uuid) {
        concertService.validateSeatStatusForPayment(reservationId);
        Reservation reservation = concertService.convertReservationToComplete(reservationId);
        userService.updateUserPoint(userId, reservation.getTotalAmount(), PointStatus.USE);
        Payment payment = paymentService.completePayment(userId, reservation, reservation.getTotalAmount());
        waitingQueueCacheService.expireTokenAfterPayment(uuid);
        return ApplicationMapper.PaymentMapper.from(payment);
    }

}
