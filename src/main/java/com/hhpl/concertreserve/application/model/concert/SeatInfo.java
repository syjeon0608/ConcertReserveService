package com.hhpl.concertreserve.application.model.concert;

import com.hhpl.concertreserve.domain.concert.type.SeatStatus;

import java.time.LocalDateTime;

public record SeatInfo(
        Long id,
        Long scheduleId,
        int price,
        SeatStatus status,
        LocalDateTime expiredAt) {
}
