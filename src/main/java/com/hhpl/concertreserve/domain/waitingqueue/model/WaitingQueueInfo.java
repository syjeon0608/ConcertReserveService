package com.hhpl.concertreserve.domain.waitingqueue.model;

public record WaitingQueueInfo(
        Long concertId,
        String queueStatus,
        Long renamingQueueNo
) { }
