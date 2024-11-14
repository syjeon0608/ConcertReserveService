package com.hhpl.concertreserve.app.concert.api.dto;

import com.hhpl.concertreserve.app.concert.domain.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long reservationId,
        Long userId,
        Long seatId,
        ReservationStatus reservationStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}