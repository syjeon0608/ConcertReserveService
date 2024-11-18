package com.hhpl.concertreserve.app.user.interfaces.dto;

import java.time.LocalDateTime;

public record PointAmountResponse (
        Long userId,
        int amount,
        LocalDateTime updatedAt
){
}
