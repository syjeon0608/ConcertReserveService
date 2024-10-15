package com.hhpl.concertreserve.domain.waitingqueue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
