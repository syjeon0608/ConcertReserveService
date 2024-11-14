package com.hhpl.concertreserve.app.user.domain;

import java.time.LocalDateTime;

public record PointInfo(
        Long userId,
        int amount,
        LocalDateTime updatedAt
) {
}
