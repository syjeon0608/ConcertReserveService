package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ApplicationMapper;
import com.hhpl.concertreserve.application.model.waitingqueue.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueCacheService;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {

    private final WaitingQueueCacheService waitingQueueCacheService;


    public void enterWaitingQueue(String uuid){
        waitingQueueCacheService.enterWaitingQueue(uuid);
    }

    public WaitingQueueInfo getWaitingQueueInfoForUser(String uuid) {
        WaitingQueue waitingQueue =  waitingQueueCacheService.getQueuePosition(uuid);
        return ApplicationMapper.WaitingQueueMapper.from(waitingQueue);
    }

    public void validateWaitingQueueUuid(String uuid){
        waitingQueueCacheService.validateUuid(uuid);
    }

    public boolean validateTokenActivationForNextStep(String uuid){
        return waitingQueueCacheService.checkActiveTokenValidity(uuid);
    }

}
