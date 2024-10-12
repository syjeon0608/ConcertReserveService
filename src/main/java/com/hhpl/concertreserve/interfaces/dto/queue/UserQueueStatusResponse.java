package com.hhpl.concertreserve.interfaces.dto.queue;

public record UserQueueStatusResponse(
        Long tokenId,
        String userUuid,
        String status,
        int remainingQueueCount
) {
}