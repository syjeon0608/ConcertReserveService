package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.domain.concert.ConcertService;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.payment.PaymentService;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.user.UserService;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final UserService userService;

    @Transactional
    public Payment processPayment(Long reservationId, Long userId, int paymentAmount) {
        Reservation reservation = concertService.getReservationInfo(reservationId);
        concertService.completeReservationByPayment(reservationId);
        userService.updateUserPoint(userId, paymentAmount, PointStatus.USE);
        return paymentService.completePayment(userId,reservation, paymentAmount);
    }


}
