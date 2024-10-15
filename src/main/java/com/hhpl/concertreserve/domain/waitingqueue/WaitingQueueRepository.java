package com.hhpl.concertreserve.domain.waitingqueue;

import java.util.Optional;

public interface WaitingQueueRepository {
    Optional<Long> findMaxQueueNoByConcertId(Long concertId);
    WaitingQueue save(WaitingQueue waitingQueue);
}
