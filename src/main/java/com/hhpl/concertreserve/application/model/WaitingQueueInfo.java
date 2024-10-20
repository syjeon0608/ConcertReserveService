package com.hhpl.concertreserve.application.model;

import com.hhpl.concertreserve.domain.waitingqueue.type.WaitingQueueStatus;

public record WaitingQueueInfo(
        String uuid,
        Long concertId,
        WaitingQueueStatus queueStatus,
        Long renamingQueueNo
) { }
