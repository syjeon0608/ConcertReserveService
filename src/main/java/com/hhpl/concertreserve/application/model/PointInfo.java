package com.hhpl.concertreserve.application.model;

import java.time.LocalDateTime;

public record PointInfo(
        Long userId,
        int amount,
        LocalDateTime updatedAt
) {
}
