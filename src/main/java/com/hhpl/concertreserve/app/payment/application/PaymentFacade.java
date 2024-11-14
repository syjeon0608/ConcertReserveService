package com.hhpl.concertreserve.app.payment.application;

import com.hhpl.concertreserve.app.common.mapper.ApplicationMapper;
import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.service.ConcertService;
import com.hhpl.concertreserve.app.payment.domain.PaymentInfo;
import com.hhpl.concertreserve.app.payment.domain.entity.Payment;
import com.hhpl.concertreserve.app.payment.domain.service.PaymentService;
import com.hhpl.concertreserve.app.user.domain.PointStatus;
import com.hhpl.concertreserve.app.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final PaymentSuccessEventPublisher paymentEventPublisher;

    @Transactional
    public PaymentInfo processPayment(Long reservationId, Long userId, String token) {
        concertService.validateSeatStatusForPayment(reservationId);
        Reservation reservation = concertService.convertReservationToComplete(reservationId);
        ReservationInfo reservationInfo = ApplicationMapper.ConcertMapper.from(reservation);
        userService.updateUserPoint(userId, reservation.getTotalAmount(), PointStatus.USE);
        Payment payment = paymentService.completePayment(userId, reservation, reservation.getTotalAmount());

        paymentEventPublisher.success(reservationInfo, token);
        return ApplicationMapper.PaymentMapper.from(payment);
    }

}
