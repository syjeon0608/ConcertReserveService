package com.hhpl.concertreserve.interfaces.dto.payment;

import java.time.LocalDateTime;

public record WalletAmountResponse(
        Long userId,
        Long balance,
        LocalDateTime lastCheckedAt) {
}
