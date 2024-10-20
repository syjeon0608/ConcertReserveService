package com.hhpl.concertreserve.interfaces.dto.waitingqueue;

public record WaitingQueueResponse(
        Long concertId,
        String queueStatus,
        Long renamingQueueNo
) {
}