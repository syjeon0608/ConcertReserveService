package com.hhpl.concertreserve.infra.database.waitingqueue;

import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COALESCE(MAX(t.queueNo), 0) FROM WaitingQueue t WHERE t.concertId = :concertId")
    Optional<Long> findMaxQueueNoByConcertId(@Param("concertId") Long concertId);

    @Query("SELECT MAX(w.queueNo) FROM WaitingQueue w WHERE w.concertId = :concertId AND w.queueStatus = 'ACTIVE'")
    Optional<Long> findMaxActivatedQueueNoByConcertId(@Param("concertId") Long concertId);

    Optional<WaitingQueue> findByUuidAndConcertId(String uuid, Long concertId);

    @Query("SELECT DISTINCT w.concertId FROM WaitingQueue w WHERE w.queueStatus = 'INACTIVE'")
    List<Long> findAllConcertIdsWithInactiveQueues();

    @Query("SELECT w FROM WaitingQueue w WHERE w.queueStatus = 'INACTIVE' AND w.concertId = :concertId ORDER BY w.queueNo ASC")
    List<WaitingQueue> findInactiveQueuesForActivationByConcertId(@Param("concertId") Long concertId, Pageable pageable);

    @Query("SELECT w FROM WaitingQueue w WHERE w.queueStatus = 'ACTIVE' AND w.expiredAt < CURRENT_TIMESTAMP")
    List<WaitingQueue> findActiveQueuesForExpiration();

    @Query("SELECT w FROM WaitingQueue w WHERE w.uuid = :uuid AND w.queueStatus = 'ACTIVE'")
    Optional<WaitingQueue> findActiveWaitingQueueByUuid(@Param("uuid") String uuid);
}
