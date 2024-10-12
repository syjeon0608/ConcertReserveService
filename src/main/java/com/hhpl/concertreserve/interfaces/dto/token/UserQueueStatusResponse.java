package com.hhpl.concertreserve.interfaces.dto.token;

public record UserQueueStatusResponse(
        Long tokenId,
        String userUuid,
        String status,
        int remainingQueueCount
) {
}