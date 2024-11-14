package com.hhpl.concertreserve.app.concert.api.dto;

import com.hhpl.concertreserve.app.concert.domain.ReservationStatus;
import com.hhpl.concertreserve.app.user.domain.entity.User;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long reservationId,
        User user,
        Long seatId,
        ReservationStatus reservationStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}