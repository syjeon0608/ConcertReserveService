package com.hhpl.concertreserve.interfaces.dto.concert;

import com.hhpl.concertreserve.domain.concert.type.SeatStatus;

import java.time.LocalDateTime;

public record SeatResponse(
        Long id,
        Long scheduleId,
        int price,
        SeatStatus status,
        LocalDateTime expiredAt
) {}
