package com.hhpl.concertreserve.domain.user;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.user.model.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hhpl.concertreserve.domain.error.ErrorType.*;
import static com.hhpl.concertreserve.domain.user.model.PointStatus.CHARGE;
import static com.hhpl.concertreserve.domain.user.model.PointStatus.USE;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    @DisplayName("충전한 금액만큼 잔액이 증가해야 한다.")
    void testChargeIncreasesAmount() {
        Point point = new Point(1L, 10000);
        int chargeToAmount = 50000;

        point.adjust(chargeToAmount, CHARGE);

        assertEquals(60000, point.getAmount());
        assertNotNull(point.getUpdatedAt());
    }

    @Test
    @DisplayName("충전 금액은 0원 이상이여야한다.")
    void shouldThrowExceptionWhenChargeAmountIsZero() {
        Point point = new Point(1L, 10000);
        int amountToCharge = 0;

        CoreException exception = assertThrows(CoreException.class, () -> {
            point.validate(amountToCharge,CHARGE);
        });

        assertEquals(INVALID_CHARGE_AMOUNT, exception.getErrorType());
    }

    @Test
    @DisplayName("충전 금액은 음수면 안된다.")
    void shouldThrowExceptionWhenChargeAmountIsNegative() {
        Point point = new Point(1L, 10000);
        int amountToCharge = -1000;

        CoreException exception = assertThrows(CoreException.class, () -> {
            point.validate(amountToCharge,CHARGE);
        });

        assertEquals(INVALID_CHARGE_AMOUNT, exception.getErrorType());
    }

    @Test
    @DisplayName("포인트 충전 시 잔액이 최대 한도를 초과하면 예외가 발생한다.")
    void shouldThrowExceptionIfPointExceedsMaximumLimitAfterCharge(){
        Point point = new Point(1L, 80000000);
        int amountToCharge = 2000000;

        CoreException exception = assertThrows(CoreException.class, () -> {
            point.validate(amountToCharge,CHARGE);
        });

        assertEquals(EXCEEDS_MAXIMUM_POINT, exception.getErrorType());
    }


    @Test
    @DisplayName("잔액이 부족하면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenBalanceIsInsufficient() {

        Point point = new Point(1L, 50000);
        int amountToUse = 60000;

        CoreException exception = assertThrows(CoreException.class, () -> {
            point.validate(amountToUse, USE);
        });

        assertEquals(POINT_NOT_ENOUGH, exception.getErrorType());
    }

    @Test
    @DisplayName("결제 후 잔액에서 결제 금액만큼 차감한다.")
    void shouldDeductAmountFromBalanceAfterPayment() {
        Point point = new Point(1L, 70000);
        int amountToUse = 20000;

        point.adjust(amountToUse,USE);

        assertEquals(50000, point.getAmount());
    }

}