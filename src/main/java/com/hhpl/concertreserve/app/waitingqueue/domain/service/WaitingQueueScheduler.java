package com.hhpl.concertreserve.app.waitingqueue.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueScheduler {

    private final WaitingQueueCacheService waitingQueueCacheService;

    private final int TOKEN_ACTIVATION_LIMIT = 10 ;
    private final int EXPIRE_TIME = 1200000;

    @Scheduled(fixedRate = 2000)
    public void activateWaitingQueues() {
        waitingQueueCacheService.activateTokens(TOKEN_ACTIVATION_LIMIT,EXPIRE_TIME);
    }

}
