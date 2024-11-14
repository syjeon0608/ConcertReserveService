package com.hhpl.concertreserve.app.user.api.dto;

import java.time.LocalDateTime;

public record PointAmountResponse (
        Long userId,
        int amount,
        LocalDateTime updatedAt
){
}
