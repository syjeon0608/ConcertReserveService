package com.hhpl.concertreserve.app.payment.domain.service;


import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.payment.domain.entity.Payment;
import com.hhpl.concertreserve.app.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment completePayment(Long userId, Reservation reservation, int amount) {
        Payment payment = new Payment(userId, reservation, amount);
        return paymentRepository.save(payment);
    }

}
