package com.hhpl.concertreserve.app.payment.infra.database;

import com.hhpl.concertreserve.app.payment.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {
}
