package com.hhpl.concertreserve.infra.waitingqueue;

import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueue;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
