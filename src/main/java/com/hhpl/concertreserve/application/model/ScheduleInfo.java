package com.hhpl.concertreserve.application.model;


import java.time.LocalDateTime;

public record ScheduleInfo(
        Long id,
        Long concertId,
        LocalDateTime concertDate,
        int availableSeats,
        int totalSeats) {
}