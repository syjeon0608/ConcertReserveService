package com.hhpl.concertreserve.domain.waitingqueue;

public record WaitingQueueInfo(
        Long concertId,
        String queueStatus,
        Long renamingQueueNo
) { }
