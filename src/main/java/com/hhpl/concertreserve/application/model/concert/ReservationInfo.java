package com.hhpl.concertreserve.application.model.concert;

import com.hhpl.concertreserve.domain.concert.type.ReservationStatus;
import com.hhpl.concertreserve.domain.user.model.User;

import java.time.LocalDateTime;

public record ReservationInfo(
        Long id,
        User user,
        Long seatId,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}