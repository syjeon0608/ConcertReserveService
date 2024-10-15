package com.hhpl.concertreserve.domain.payment;

public interface WalletRepository {
    Wallet getWallet(Long userId);
    Wallet updateWallet(Wallet wallet);
}
