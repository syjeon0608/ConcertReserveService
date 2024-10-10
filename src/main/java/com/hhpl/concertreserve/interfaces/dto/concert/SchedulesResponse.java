package com.hhpl.concertreserve.interfaces.dto.concert;

import java.time.LocalDateTime;
import java.util.List;

public record SchedulesResponse(
        Long concertId,
        String concertTitle,
        String concertDescription,
        LocalDateTime openDate,
        List<ScheduleInfo> schedules
) {
}