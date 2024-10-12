package com.hhpl.concertreserve.interfaces.dto.concert;

import java.time.LocalDate;

public record ScheduleInfo(LocalDate date, int availableSeatsCount) {
}
