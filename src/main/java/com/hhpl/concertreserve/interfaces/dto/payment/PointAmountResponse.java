package com.hhpl.concertreserve.interfaces.dto.payment;

import java.time.LocalDateTime;

public record PointAmountResponse (
        Long userId,
        int amount,
        LocalDateTime updatedAt
){
}
