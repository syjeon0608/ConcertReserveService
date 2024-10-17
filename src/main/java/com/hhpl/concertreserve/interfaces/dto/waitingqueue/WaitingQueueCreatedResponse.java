package com.hhpl.concertreserve.interfaces.dto.waitingqueue;

import java.time.LocalDateTime;

public record WaitingQueueCreatedResponse(
        String uuid,
        Long concertId,
        LocalDateTime createdAt
) {
}