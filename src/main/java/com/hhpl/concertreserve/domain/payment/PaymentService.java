package com.hhpl.concertreserve.domain.payment;


import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.payment.model.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment completePayment(Long userId, Reservation reservation, int amount) {
        Payment payment = new Payment(userId,reservation, amount);
        return paymentRepository.save(payment);
    }

    public Point getPointInfo(Long userId) {
        return paymentRepository.getWallet(userId);
    }

    @Transactional
    public Point chargeMyPoint(Long userId, int amountToCharge) {
        Point point = paymentRepository.getWallet(userId);
        point.chargePoints(amountToCharge);
        return paymentRepository.updatePoint(point);
    }

    public void subtractPointsForPayment(Long userId, int amountToUse) {
        Point point = getPointInfo(userId);
        point.checkPointsForPayment(amountToUse);
        point.usePointToPayment(amountToUse);
        paymentRepository.updatePoint(point);
    }

}
