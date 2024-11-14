package com.hhpl.concertreserve.app.concert.domain;


import java.time.LocalDateTime;

public record ScheduleInfo(
        Long id,
        Long concertId,
        LocalDateTime concertDate,
        int availableSeats,
        int totalSeats) {
}