package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.payment.model.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.INVALID_CHARGE_AMOUNT;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.POINT_NOT_ENOUGH;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    @DisplayName("충전한 금액만큼 잔액이 증가해야 한다.")
    void testChargeIncreasesAmount() {
        Point point = new Point(1L, 10000);
        int chargeToAmount = 50000;

        point.chargePoints(chargeToAmount);

        assertEquals(60000, point.getAmount());
        assertNotNull(point.getUpdatedAt());
    }

    @Test
    @DisplayName("충전 금액은 0원 이상이여야한다.")
    void shouldThrowExceptionWhenChargeAmountIsZero() {
        Point point = new Point(1L, 10000);
        int amountToCharge = 0;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            point.chargePoints(amountToCharge);
        });

        assertEquals(INVALID_CHARGE_AMOUNT, exception.getErrorCode());
    }

    @Test
    @DisplayName("충전 금액은 음수면 안된다.")
    void shouldThrowExceptionWhenChargeAmountIsNegative() {
        Point point = new Point(1L, 10000);
        int amountToCharge = -1000;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            point.chargePoints(amountToCharge);
        });

        assertEquals(INVALID_CHARGE_AMOUNT, exception.getErrorCode());
    }


    @Test
    @DisplayName("잔액이 부족하면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        Point point = new Point(1L, 50000);
        int amountToUse = 60000;

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            point.checkPointsForPayment(amountToUse);
        });

        assertEquals(POINT_NOT_ENOUGH, exception.getErrorCode());
    }

    @Test
    @DisplayName("결제 후 잔액에서 결제 금액만큼 차감한다.")
    void shouldDeductAmountFromBalanceAfterPayment() {
        Point point = new Point(1L, 70000);
        int amountToUse = 20000;

        point.usePointToPayment(amountToUse);

        assertEquals(50000, point.getAmount());
    }

}