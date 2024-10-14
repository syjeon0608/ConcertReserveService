package com.hhpl.concertreserve.interfaces.dto.token;

import java.time.LocalDateTime;

public record TokenResponse(
        Long tokenId,
        String uuid,
        Long concertId,
        Long queueNo,
        String tokenStatus,
        LocalDateTime createdAt
) {
}