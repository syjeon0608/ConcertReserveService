package com.hhpl.concertreserve.app.concert.api.dto;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        Long concertId,
        LocalDateTime concertDate,
        int availableSeats,
        int totalSeats
) {}