package com.hhpl.concertreserve.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWalletInfo(Long userId) {
        return walletRepository.getWallet(userId);
    }

    public Wallet chargeMyWallet(Long userId, int Amount) {
        Wallet wallet = walletRepository.getWallet(userId);
        wallet.charge(Amount);
        return walletRepository.updateWallet(wallet);
    }

}
