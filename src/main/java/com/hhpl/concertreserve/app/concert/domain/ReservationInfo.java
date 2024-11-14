package com.hhpl.concertreserve.app.concert.domain;

import com.hhpl.concertreserve.app.user.domain.entity.User;

import java.time.LocalDateTime;

public record ReservationInfo(
        Long id,
        User user,
        Long seatId,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}