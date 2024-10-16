package com.hhpl.concertreserve.domain.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    private final WaitingQueueRepository waitingQueueRepository;
    private final WaitingQueueValidator waitingQueueValidate;

    public Long getMaxWaitingQueueNoByConcert(Long concertId){
        return waitingQueueRepository.findMaxQueueNoByConcertId(concertId).orElse(0L);
    }

    public WaitingQueue enterWaitingQueue(String uuid, Long concertId) {
        waitingQueueValidate.validateConcertId(concertId);
        waitingQueueValidate.validateUserUuid(uuid);

        Long maxQueueNo = getMaxWaitingQueueNoByConcert(concertId);
        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);
        return waitingQueueRepository.save(waitingQueue);
    }


    public Long getLastActivatedQueueNo(Long concertId) {
        return waitingQueueRepository. getMaxActivatedQueueNoByConcertId(concertId).orElse(0L);
    }

    public WaitingQueueInfo getMyWaitingQueueInfo(String uuid, Long concertId) {
        WaitingQueue myWaitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId);

        Long lastActivatedQueueNo = getLastActivatedQueueNo(concertId);
        return myWaitingQueue.getWaitingQueueInfo(lastActivatedQueueNo);
    }

    public void validateQueueStatus(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId);
        waitingQueue.validate();
    }

    public void expireWaitingQueue() {
        List<WaitingQueue> expiredQueues = waitingQueueRepository.findActiveQueuesForExpiration();

        expiredQueues.forEach(queue -> {
            queue.updateStatusIfExpired();
            waitingQueueRepository.save(queue);
        });
    }

    public void makeWaitingQueueExpiredWhenPaymentComplete(String uuid) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyActiveQueue(uuid);
        waitingQueue.makeExpiredWhenCompletePayment();
        waitingQueueRepository.save(waitingQueue);
    }
}
