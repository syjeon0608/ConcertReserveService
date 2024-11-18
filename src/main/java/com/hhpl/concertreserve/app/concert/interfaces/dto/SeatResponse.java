package com.hhpl.concertreserve.app.concert.interfaces.dto;

import com.hhpl.concertreserve.app.concert.domain.SeatStatus;

import java.time.LocalDateTime;

public record SeatResponse(
        Long id,
        Long scheduleId,
        int price,
        SeatStatus status,
        LocalDateTime expiredAt
) {}
