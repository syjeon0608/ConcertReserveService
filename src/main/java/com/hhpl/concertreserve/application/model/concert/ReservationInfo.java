package com.hhpl.concertreserve.application.model.concert;

import com.hhpl.concertreserve.domain.concert.type.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationInfo(
        Long id, String uuid,
        Long seatId,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}