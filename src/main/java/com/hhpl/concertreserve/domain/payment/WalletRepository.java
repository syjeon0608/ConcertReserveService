package com.hhpl.concertreserve.domain.payment;

public interface WalletRepository {
    Wallet getWallet(String uuid);
    Wallet updateWallet(Wallet wallet);
}
