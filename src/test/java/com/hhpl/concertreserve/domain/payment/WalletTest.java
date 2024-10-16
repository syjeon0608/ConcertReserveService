package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.error.BusinessExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.INVALID_CHARGE_AMOUNT;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.POINT_NOT_ENOUGH;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    @DisplayName("충전한 금액만큼 잔액이 증가해야 한다.")
    void testChargeIncreasesAmount() {
        Wallet wallet = new Wallet(1L, 10000);
        int chargeToAmount = 50000;

        wallet.charge(chargeToAmount);

        assertEquals(60000, wallet.getAmount());
        assertNotNull(wallet.getUpdatedAt());
    }

    @Test
    @DisplayName("충전 금액은 0원 이상이여야한다.")
    void shouldThrowExceptionWhenChargeAmountIsZero() {
        Wallet wallet = new Wallet(1L, 10000);
        int amountToCharge = 0;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            wallet.charge(amountToCharge);
        });

        assertEquals(INVALID_CHARGE_AMOUNT, exception.getErrorCode());
    }

    @Test
    @DisplayName("충전 금액은 음수면 안된다.")
    void shouldThrowExceptionWhenChargeAmountIsNegative() {
        Wallet wallet = new Wallet(1L, 10000);
        int amountToCharge = -1000;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            wallet.charge(amountToCharge);
        });

        assertEquals(INVALID_CHARGE_AMOUNT, exception.getErrorCode());
    }


    @Test
    @DisplayName("잔액이 부족하면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        Wallet wallet = new Wallet(1L, 50000);
        int amountToUse = 60000;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            wallet.checkCanUse(amountToUse);
        });

        assertEquals(POINT_NOT_ENOUGH, exception.getErrorCode());
    }

    @Test
    @DisplayName("결제 후 잔액에서 결제 금액만큼 차감한다.")
    void shouldDeductAmountFromBalanceAfterPayment() {
        Wallet wallet = new Wallet(1L, 70000);
        int amountToUse = 20000;

        wallet.useToPayment(amountToUse);

        assertEquals(50000,wallet.getAmount());
    }

}