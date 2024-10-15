package com.hhpl.concertreserve.infra.waitingqueue;

import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
    @Query("SELECT COALESCE(MAX(t.queueNo), 0) FROM WaitingQueue t WHERE t.concertId = :concertId")
    Optional<Long> findMaxQueueNoByConcertId(@Param("concertId") Long concertId);

    @Query("SELECT MAX(w.queueNo) FROM WaitingQueue w WHERE w.concertId = :concertId AND w.queueStatus = 'ACTIVE'")
    Optional<Long> findMaxActivatedQueueNoByConcertId(@Param("concertId") Long concertId);

    Optional<WaitingQueue> findByUuidAndConcertId(String uuid, Long concertId);
}
