package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.domain.concert.ConcertService;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.payment.PaymentService;
import com.hhpl.concertreserve.domain.payment.model.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ConcertService concertService;
    private final PaymentService paymentService;

    @Transactional
    public Payment processPayment(Long reservationId, Long userId, int paymentAmount) {
        Reservation reservation = concertService.getReservationInfo(reservationId);
        concertService.validateSeatStatus(reservationId);
        paymentService.subtractPointsForPayment(userId, paymentAmount);
        concertService.completeReservationByPayment(reservationId);
       return paymentService.completePayment(userId,reservation, paymentAmount);
    }

    public Point getUserWallet(Long userId) {
        return paymentService.getPointInfo(userId);
    }

    public Point chargePoint(Long userId, int amountToCharge) {
        return paymentService.chargeMyPoint(userId, amountToCharge);
    }

}
