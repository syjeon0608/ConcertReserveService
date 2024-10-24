package com.hhpl.concertreserve.domain.payment;


import com.hhpl.concertreserve.domain.concert.ConcertRepository;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ConcertRepository concertRepository;

    @Transactional
    public Payment completePayment(Long userId, Reservation reservation, int amount) {
        reservation.completeReservation();
        concertRepository.save(reservation);
        Payment payment = new Payment(userId, reservation, amount);
        return paymentRepository.save(payment);
    }

}
