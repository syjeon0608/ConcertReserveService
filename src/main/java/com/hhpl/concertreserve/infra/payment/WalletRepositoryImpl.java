package com.hhpl.concertreserve.infra.payment;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.payment.Wallet;
import com.hhpl.concertreserve.domain.payment.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.WALLET_NOT_FOUND_ERROR;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletJpaRepository walletJpaRepository;

    @Override
    public Wallet getWallet(String uuid) {
        return walletJpaRepository.findByUuid(uuid)
                .orElseThrow(()-> new BusinessException(WALLET_NOT_FOUND_ERROR));
    }

    @Override
    public Wallet updateWallet(Wallet wallet) {
        return walletJpaRepository.save(wallet);
    }

}
