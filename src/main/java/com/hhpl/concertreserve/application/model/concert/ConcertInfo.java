package com.hhpl.concertreserve.application.model.concert;

import java.time.LocalDateTime;

public record ConcertInfo(
        Long id,
        String title,
        String description,
        LocalDateTime openDate,
        LocalDateTime startDate,
        LocalDateTime endDate) {
}
