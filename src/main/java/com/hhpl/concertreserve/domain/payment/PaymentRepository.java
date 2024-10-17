package com.hhpl.concertreserve.domain.payment;

public interface PaymentRepository {
    Payment save(Payment payment);
    Point getWallet(Long userId);
    Point updatePoint(Point point);
}
