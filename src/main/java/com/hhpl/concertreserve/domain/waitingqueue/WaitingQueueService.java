package com.hhpl.concertreserve.domain.waitingqueue;

import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WaitingQueueValidator waitingQueueValidate;

    public void validateOnlyUuid(String uuid){
        waitingQueueValidate.validateUserUuid(uuid);
    }

    public void validateUuidAndConcertId(String uuid, Long concertId){
        validateOnlyUuid(uuid);
        waitingQueueValidate.validateConcertId(concertId);
    }

    @Transactional
    public WaitingQueue enterWaitingQueue(String uuid, Long concertId) {
        List<WaitingQueue>  waitingQueues = waitingQueueRepository.findMaxQueueNoByConcertId(concertId);
        Long maxQueueNo = waitingQueues.stream()
                .map(WaitingQueue::getQueueNo)
                .max(Long::compareTo)
                .orElse(0L);
        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);
       return waitingQueueRepository.save(waitingQueue);
    }

    public Long getLastActivatedQueueNo(Long concertId) {
        return waitingQueueRepository.getMaxActivatedQueueNoByConcertId(concertId).orElse(0L);
    }

    public WaitingQueue getMyWaitingQueueInfo(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId);
        waitingQueue.ensureWaitingQueueIsNotExpired();
        Long lastActivatedQueueNo = getLastActivatedQueueNo(concertId);
        waitingQueue.calculateRemainingQueueNo(lastActivatedQueueNo);
        return waitingQueue;
    }

    public boolean checkWaitingQueueValidity(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId);
        waitingQueue.ensureWaitingQueueIsActive();
        waitingQueue.ensureWaitingQueueIsNotExpired();
        return true;
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
            queue.expire();
            waitingQueueRepository.save(queue);
        });
    }

    public void expireQueueOnPaymentCompletion(String uuid) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyActiveQueue(uuid);
        waitingQueue.expireOnPaymentCompletion();
        waitingQueueRepository.save(waitingQueue);
    }
}
