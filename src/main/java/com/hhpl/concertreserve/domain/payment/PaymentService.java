package com.hhpl.concertreserve.domain.payment;


import com.hhpl.concertreserve.domain.concert.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void completePayment(Reservation reservation, int amount) {
        Payment payment = new Payment(reservation, amount);
        paymentRepository.save(payment);
    }
}
