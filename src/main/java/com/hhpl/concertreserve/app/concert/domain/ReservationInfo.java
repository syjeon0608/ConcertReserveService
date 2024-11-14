package com.hhpl.concertreserve.app.concert.domain;

import java.time.LocalDateTime;

public record ReservationInfo(
        Long id,
        Long userId,
        Long seatId,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}