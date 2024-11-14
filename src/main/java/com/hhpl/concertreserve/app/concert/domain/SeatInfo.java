package com.hhpl.concertreserve.app.concert.domain;

import java.time.LocalDateTime;

public record SeatInfo(
        Long id,
        Long scheduleId,
        int price,
        SeatStatus status,
        LocalDateTime expiredAt) {
}
