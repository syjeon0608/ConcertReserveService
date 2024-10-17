package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.payment.model.Point;

public interface PaymentRepository {
    Payment save(Payment payment);
    Point getWallet(Long userId);
    Point updatePoint(Point point);
}
