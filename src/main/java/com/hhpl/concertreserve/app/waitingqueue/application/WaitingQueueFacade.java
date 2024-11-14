package com.hhpl.concertreserve.app.waitingqueue.application;

import com.hhpl.concertreserve.app.waitingqueue.domain.WaitingQueue;
import com.hhpl.concertreserve.app.waitingqueue.domain.service.WaitingQueueCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingQueueFacade {

    private final WaitingQueueCacheService waitingQueueCacheService;


    public void enterWaitingQueue(String uuid){
        waitingQueueCacheService.enterWaitingQueue(uuid);
    }

    public WaitingQueue getWaitingQueueInfoForUser(String uuid) {
        return  waitingQueueCacheService.getQueuePosition(uuid);
    }

    public void validateWaitingQueueUuid(String uuid){
        waitingQueueCacheService.validateUuid(uuid);
    }

    public boolean validateTokenActivationForNextStep(String uuid){
        return waitingQueueCacheService.checkActiveTokenValidity(uuid);
    }

}
