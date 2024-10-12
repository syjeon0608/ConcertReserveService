package com.hhpl.concertreserve.interfaces.dto.queue;

public record QueueStatusResponse(
        Long tokenId,
        String userUuid,
        String status,
        int remainingQueueCount
) {
}