package com.hhpl.concertreserve.interfaces.dto.waitingqueue;

public record WaitingQueueStatusResponse(
        Long tokenId,
        String userUuid,
        String status,
        int remainingQueueCount
) {
}