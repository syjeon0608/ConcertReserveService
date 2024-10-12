package com.hhpl.concertreserve.interfaces.dto.queue;

public record TokenValidationResponse(
        boolean isValid,
        Long tokenId,
        String uuid
) {
}