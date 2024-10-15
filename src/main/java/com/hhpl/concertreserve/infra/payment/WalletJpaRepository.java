package com.hhpl.concertreserve.infra.payment;

import com.hhpl.concertreserve.domain.payment.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletJpaRepository extends JpaRepository<Wallet,Long> {
}
