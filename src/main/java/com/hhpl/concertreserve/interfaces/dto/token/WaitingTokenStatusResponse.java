package com.hhpl.concertreserve.interfaces.dto.token;

public record WaitingTokenStatusResponse(
        Long tokenId,
        String userUuid,
        String status,
        int remainingQueueCount
) {
}