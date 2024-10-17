package com.hhpl.concertreserve.interfaces.dto.concert;

import java.time.LocalDateTime;

public record ConcertResponse(
        Long id,
        String title,
        String description,
        LocalDateTime openDate,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
