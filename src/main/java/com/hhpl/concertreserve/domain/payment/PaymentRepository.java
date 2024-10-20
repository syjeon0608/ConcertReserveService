package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.payment.model.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
