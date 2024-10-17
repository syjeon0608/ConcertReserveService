package com.hhpl.concertreserve.interfaces.dto.concert;

import com.hhpl.concertreserve.domain.concert.type.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long reservationId,
        String uuid,
        Long seatId,
        ReservationStatus reservationStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}