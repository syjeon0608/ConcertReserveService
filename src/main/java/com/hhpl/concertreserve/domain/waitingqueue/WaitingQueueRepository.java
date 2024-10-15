package com.hhpl.concertreserve.domain.waitingqueue;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository {
    Optional<Long> findMaxQueueNoByConcertId(Long concertId);
    WaitingQueue save(WaitingQueue waitingQueue);

    Optional<Long> getMaxActivatedQueueNoByConcertId(Long concertId);

    Optional<WaitingQueue> getMyWaitingQueue(String uuid, Long concertId);

    List<Long> findAllConcertIdsWithInactiveQueues();

    List<WaitingQueue> findInactiveQueuesForActivation(Long concertId, int activationLimit);

    List<WaitingQueue> findActiveQueuesForExpiration();


}
