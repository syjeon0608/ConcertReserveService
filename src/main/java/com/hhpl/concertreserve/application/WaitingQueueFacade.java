package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;

    public WaitingQueue enterWaitingQueueForConcert(String uuid, Long concertId){
        waitingQueueService.validateUserBeforeQueueEntry(uuid, concertId);
        return waitingQueueService.enterWaitingQueue(uuid, concertId);
    }

    public WaitingQueueInfo getWaitingQueueInfoForUser(String uuid, Long concertId) {
        return waitingQueueService.getMyWaitingQueueInfo(uuid, concertId);
    }

    public void validateQueueStatusForUser(String uuid, Long concertId){
        waitingQueueService.validateQueueStatus(uuid,concertId);
    }

}
