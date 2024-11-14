package com.hhpl.concertreserve.app.payment.domain.repository;

import com.hhpl.concertreserve.app.payment.domain.entity.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
