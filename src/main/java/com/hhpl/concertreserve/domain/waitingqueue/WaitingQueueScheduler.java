package com.hhpl.concertreserve.domain.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueScheduler {

    private final WaitingQueueRepository waitingQueueRepository;

    @Scheduled(fixedRate = 30000)
    public void activateWaitingQueuesForAllConcerts() {
        List<Long> concertIds = waitingQueueRepository.findAllConcertIdsWithInactiveQueues();

        concertIds.forEach(this::activateWaitingQueues);
    }

    public void activateWaitingQueues(Long concertId) {
        int activationLimit = 5;

        List<WaitingQueue> inactiveQueues = waitingQueueRepository.findInactiveQueuesForActivation(concertId,activationLimit);

        inactiveQueues.forEach(queue -> {
            queue.activate();
            waitingQueueRepository.save(queue);
        });
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndExpireTokens() {

        List<WaitingQueue> activeQueuesForExpiration = waitingQueueRepository.findActiveQueuesForExpiration();

        activeQueuesForExpiration.forEach(queue -> {
            queue.updateStatusIfExpired();
            waitingQueueRepository.save(queue);
        });
    }
}
