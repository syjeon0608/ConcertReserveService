package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ApplicationMapper;
import com.hhpl.concertreserve.application.model.waitingqueue.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueInfo enterWaitingQueueForConcert(String uuid, Long concertId){
        waitingQueueService.validateUuidAndConcertId(uuid, concertId);
        WaitingQueue waitingQueue =  waitingQueueService.enterWaitingQueue(uuid, concertId);
        return ApplicationMapper.WaitingQueueMapper.from(waitingQueue);
    }

    public WaitingQueueInfo getWaitingQueueInfoForUser(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueService.getMyWaitingQueueInfo(uuid, concertId);
        return ApplicationMapper.WaitingQueueMapper.from(waitingQueue);
    }

    public boolean validateQueueReadinessForSubsequent(String uuid, Long concertId){
        waitingQueueService.validateUuidAndConcertId(uuid, concertId);
       return waitingQueueService.checkWaitingQueueValidity(uuid,concertId);
    }

    public void validateWaitingQueueUuid(String uuid){
        waitingQueueService.validateOnlyUuid(uuid);
    }

}
