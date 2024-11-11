package com.hhpl.concertreserve.interfaces.dto.concert;

import com.hhpl.concertreserve.domain.concert.type.ReservationStatus;
import com.hhpl.concertreserve.domain.user.model.User;

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