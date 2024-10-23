package com.hhpl.concertreserve.infra.database.payment;

import com.hhpl.concertreserve.domain.payment.PaymentRepository;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

}
