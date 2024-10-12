package com.hhpl.concertreserve.interfaces.dto.payment;

import java.time.LocalDateTime;

public record WalletChargeResponse(
        Long userId,
        Long chargedAmount,
        LocalDateTime chargedAt,
        Long updatedAmount
) {
}
