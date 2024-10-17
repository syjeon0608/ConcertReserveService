package com.hhpl.concertreserve.interfaces.dto.waitingqueue;

public record WaitingQueueStatusResponse(
        Long concertId,
        String queueStatus,
        Long renamingQueueNo
) {
}