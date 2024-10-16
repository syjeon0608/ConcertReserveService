package com.hhpl.concertreserve.infra.payment;

import com.hhpl.concertreserve.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {
}
