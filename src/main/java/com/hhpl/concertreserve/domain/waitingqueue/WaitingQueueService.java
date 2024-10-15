package com.hhpl.concertreserve.domain.waitingqueue;

import com.hhpl.concertreserve.domain.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.*;

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
        WaitingQueue myWaitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId)
                .orElseThrow(() -> new BusinessException(QUEUE_NOT_FOUND));

        Long lastActivatedQueueNo = getLastActivatedQueueNo(concertId);
        return myWaitingQueue.getWaitingQueueInfo(lastActivatedQueueNo);
    }

    public void validateQueueStatus(String uuid, Long concertId) {
        WaitingQueue waitingQueue = waitingQueueRepository.getMyWaitingQueue(uuid,concertId)
                .orElseThrow(() -> new BusinessException(QUEUE_NOT_FOUND));

        if (waitingQueue.getQueueStatus() != WaitingQueueStatus.ACTIVE) {
            throw new BusinessException(QUEUE_IS_INACTIVE);
        }

        if (waitingQueue.isExpired()) {
            throw new BusinessException(QUEUE_IS_EXPIRED);
        }
    }
}
