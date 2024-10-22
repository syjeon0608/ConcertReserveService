package com.hhpl.concertreserve.application.model.waitingqueue;

import com.hhpl.concertreserve.domain.waitingqueue.type.WaitingQueueStatus;

public record WaitingQueueInfo(
        String uuid,
        Long concertId,
        WaitingQueueStatus queueStatus,
        Long renamingQueueNo
) { }
