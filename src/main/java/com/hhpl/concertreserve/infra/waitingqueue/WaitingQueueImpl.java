package com.hhpl.concertreserve.infra.waitingqueue;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueue;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.QUEUE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class WaitingQueueImpl implements WaitingQueueRepository {
    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Override
    public Optional<Long> findMaxQueueNoByConcertId(Long concertId) {
        return waitingQueueJpaRepository.findMaxQueueNoByConcertId(concertId);
    }

    @Override
    public WaitingQueue save(WaitingQueue waitingQueue) {
        return waitingQueueJpaRepository.save(waitingQueue);
    }

    @Override
    public Optional<Long> getMaxActivatedQueueNoByConcertId(Long concertId) {
        return waitingQueueJpaRepository.findMaxActivatedQueueNoByConcertId(concertId);
    }

    @Override
    public WaitingQueue getMyWaitingQueue(String uuid, Long concertId) {
        return waitingQueueJpaRepository.findByUuidAndConcertId(uuid, concertId)
                .orElseThrow(() -> new BusinessException(QUEUE_NOT_FOUND));
    }

    @Override
    public List<Long> findAllConcertIdsWithInactiveQueues() {
        return waitingQueueJpaRepository.findAllConcertIdsWithInactiveQueues();
    }

    @Override
    public List<WaitingQueue> findInactiveQueuesForActivation(Long concertId, int activationLimit) {
        Pageable pageable = PageRequest.of(0, activationLimit);
        return waitingQueueJpaRepository.findInactiveQueuesForActivationByConcertId(concertId, pageable);
    }

    @Override
    public List<WaitingQueue> findActiveQueuesForExpiration() {
        return waitingQueueJpaRepository.findActiveQueuesForExpiration();
    }

}
