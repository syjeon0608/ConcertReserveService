package com.hhpl.concertreserve.domain.payment;


import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment completePayment(Long userId, Reservation reservation, int amount) {
        Payment payment = new Payment(userId,reservation, amount);
        return paymentRepository.save(payment);
    }

}
