package com.hhpl.concertreserve.domain.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WaitingQueueValidator waitingQueueValidate;

    public Long getMaxWaitingQueueNoByConcert(Long concertId){
        return waitingQueueRepository.findMaxQueueNoByConcertId(concertId).orElse(0L);
    }

    public void validateUserBeforeQueueEntry(String uuid, Long concertId){
        waitingQueueValidate.validateConcertId(concertId);
        waitingQueueValidate.validateUserUuid(uuid);
    }

    @Transactional
    public WaitingQueue enterWaitingQueue(String uuid, Long concertId) {
        Long maxQueueNo = getMaxWaitingQueueNoByConcert(concertId);
        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);
       return waitingQueueRepository.save(waitingQueue);
    }

    public Long getLastActivatedQueueNo(Long concertId) {
        return waitingQueueRepository.getMaxActivatedQueueNoByConcertId(concertId).orElse(0L);
    }

    public WaitingQueueInfo getMyWaitingQueueInfo(String uuid, Long concertId) {
        WaitingQueue myWaitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId);

        Long lastActivatedQueueNo = getLastActivatedQueueNo(concertId);
        return myWaitingQueue.getRecentWaitingQueueInfo(lastActivatedQueueNo);
    }

    public void validateQueueStatus(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId);
        waitingQueue.validate();
    }

    public void activateInactiveQueuesForAllConcerts() {
        List<Long> concertIds = waitingQueueRepository.findAllConcertIdsWithInactiveQueues();
        concertIds.forEach(this::activateWaitingQueues);
    }

    @Transactional
    public void activateWaitingQueues(Long concertId) {
        int activationLimit = 5;
        List<WaitingQueue> inactiveQueues = waitingQueueRepository.findInactiveQueuesForActivation(concertId, activationLimit);
        inactiveQueues.forEach(queue -> {
            queue.activate();
            waitingQueueRepository.save(queue);
        });
    }

    @Transactional
    public void expireActiveQueuesPastDeadline() {
        List<WaitingQueue> expiredQueues = waitingQueueRepository.findActiveQueuesForExpiration();
        expiredQueues.forEach(queue -> {
            queue.updateStatusIfExpired();
            waitingQueueRepository.save(queue);
        });
    }

    public void expireQueueOnPaymentCompletion(String uuid) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyActiveQueue(uuid);
        waitingQueue.expireOnPaymentCompletion();
        waitingQueueRepository.save(waitingQueue);
    }
}
