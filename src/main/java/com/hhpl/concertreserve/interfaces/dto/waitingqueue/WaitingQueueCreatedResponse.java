package com.hhpl.concertreserve.interfaces.dto.waitingqueue;

import java.time.LocalDateTime;

public record WaitingQueueCreatedResponse(
        Long tokenId,
        String uuid,
        Long concertId,
        Long queueNo,
        String tokenStatus,
        LocalDateTime createdAt
) {
}