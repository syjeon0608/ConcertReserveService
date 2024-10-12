package com.hhpl.concertreserve.interfaces.dto.queue;

import java.time.LocalDateTime;

public record ActivationResponse(
        Long tokenId,
        String userUuid,
        String tokenStatus,
        LocalDateTime createdAt,
        LocalDateTime activatedAt
) {
}
