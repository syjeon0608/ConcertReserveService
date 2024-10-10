package com.hhpl.concertreserve.interfaces.dto.concert;

import java.util.List;

public record AvailableSeatsResponse(Long concertId, Long scheduleId, List<Integer> availableSeats) {
}