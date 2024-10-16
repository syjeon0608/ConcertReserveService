package com.hhpl.concertreserve.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWalletInfo(String uuid) {
        return walletRepository.getWallet(uuid);
    }

    public Wallet chargeMyWallet(String uuid, int amountToCharge) {
        Wallet wallet = walletRepository.getWallet(uuid);
        wallet.charge(amountToCharge);
        return walletRepository.updateWallet(wallet);
    }

    public void updateMyWalletToPayment(String uuid, int amountToUse) {
        Wallet wallet = getWalletInfo(uuid);
        wallet.checkCanUse(amountToUse);
        wallet.useToPayment(amountToUse);
        walletRepository.updateWallet(wallet);
    }

}
