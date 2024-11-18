package com.hhpl.concertreserve.app.concert.interfaces.dto;

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
