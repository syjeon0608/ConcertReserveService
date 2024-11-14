package com.hhpl.concertreserve.app.payment.infra.database;

import com.hhpl.concertreserve.app.payment.domain.entity.Payment;
import com.hhpl.concertreserve.app.payment.domain.repository.PaymentRepository;
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
