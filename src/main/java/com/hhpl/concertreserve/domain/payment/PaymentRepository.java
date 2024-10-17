package com.hhpl.concertreserve.domain.payment;

public interface PaymentRepository {
    void save(Payment payment);
    Point getWallet(Long userId);
    Point updatePoint(Point point);
}
