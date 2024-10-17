package com.hhpl.concertreserve.interfaces.dto.concert;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        Long concertId,
        LocalDateTime concertDate,
        int availableSeats,
        int totalSeats
) {}