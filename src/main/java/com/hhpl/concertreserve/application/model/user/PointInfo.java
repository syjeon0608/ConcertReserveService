package com.hhpl.concertreserve.application.model.user;

import java.time.LocalDateTime;

public record PointInfo(
        Long userId,
        int amount,
        LocalDateTime updatedAt
) {
}
