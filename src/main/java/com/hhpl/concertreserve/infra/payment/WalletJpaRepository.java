package com.hhpl.concertreserve.infra.payment;

import com.hhpl.concertreserve.domain.payment.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletJpaRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findByUuid(String uuid);
}
