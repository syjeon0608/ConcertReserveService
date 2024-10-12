package com.hhpl.concertreserve.interfaces.dto.queue;

import java.time.LocalDateTime;

public record QueueResponse(
        Long tokenId,
        String uuid,
        Long concertId,
        String tokenStatus,
        LocalDateTime createdAt
) {
}