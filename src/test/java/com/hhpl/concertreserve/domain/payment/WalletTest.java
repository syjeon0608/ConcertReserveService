package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.error.BusinessExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        assertEquals(BusinessExceptionCode.INVALID_CHARGE_AMOUNT, exception.getErrorCode());
    }

    @Test
    @DisplayName("충전 금액은 음수면 안된다.")
    void shouldThrowExceptionWhenChargeAmountIsNegative() {
        Wallet wallet = new Wallet(1L, 10000);
        int amountToCharge = -1000;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            wallet.charge(amountToCharge);
        });

        assertEquals(BusinessExceptionCode.INVALID_CHARGE_AMOUNT, exception.getErrorCode());
    }
}