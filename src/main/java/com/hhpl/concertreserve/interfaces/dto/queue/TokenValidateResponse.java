package com.hhpl.concertreserve.interfaces.dto.queue;

public record TokenValidateResponse(
        boolean isValid,
        Long tokenId,
        String uuid
) {
}