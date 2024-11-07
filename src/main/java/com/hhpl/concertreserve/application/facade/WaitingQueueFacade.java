package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ApplicationMapper;
import com.hhpl.concertreserve.application.model.waitingqueue.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueCacheService;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueuePojo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;
    private final WaitingQueueCacheService waitingQueueCacheService;


    public void enterWaitingQueue(String uuid){
        waitingQueueCacheService.enterWaitingQueue(uuid);
    }

    public WaitingQueueInfo getWaitingQueueInfoForUser(String uuid) {
        WaitingQueuePojo waitingQueue =  waitingQueueCacheService.getQueuePosition(uuid);
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
