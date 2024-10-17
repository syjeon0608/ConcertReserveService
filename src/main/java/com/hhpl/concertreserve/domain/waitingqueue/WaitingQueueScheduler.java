package com.hhpl.concertreserve.domain.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueScheduler {

    private final WaitingQueueService waitingQueueService;


    @Scheduled(fixedRate = 30000)
    public void activateWaitingQueuesForAllConcerts() {
        waitingQueueService.activateInactiveQueuesForAllConcerts();
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndExpireWaitingQueue() {
        waitingQueueService.expireActiveQueuesPastDeadline();
    }


}
