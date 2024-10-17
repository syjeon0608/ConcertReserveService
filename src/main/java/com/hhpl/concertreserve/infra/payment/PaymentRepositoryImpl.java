package com.hhpl.concertreserve.infra.payment;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.payment.Payment;
import com.hhpl.concertreserve.domain.payment.PaymentRepository;
import com.hhpl.concertreserve.domain.payment.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.WALLET_NOT_FOUND_ERROR;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;
    private final PointJpaRepository walletJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Point getWallet(Long userId) {
        return walletJpaRepository.findByUserId(userId)
                .orElseThrow(()-> new BusinessException(WALLET_NOT_FOUND_ERROR));
    }

    @Override
    public Point updatePoint(Point point) {
        return walletJpaRepository.save(point);
    }
}
