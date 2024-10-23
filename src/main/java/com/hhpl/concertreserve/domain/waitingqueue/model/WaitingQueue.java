package com.hhpl.concertreserve.domain.waitingqueue.model;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.waitingqueue.type.WaitingQueueStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.error.ErrorType.QUEUE_IS_EXPIRED;
import static com.hhpl.concertreserve.domain.error.ErrorType.QUEUE_IS_INACTIVE;
import static com.hhpl.concertreserve.domain.waitingqueue.type.WaitingQueueStatus.EXPIRED;
import static com.hhpl.concertreserve.domain.waitingqueue.type.WaitingQueueStatus.INACTIVE;

@Entity
@Getter
@NoArgsConstructor
public class WaitingQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private Long concertId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaitingQueueStatus queueStatus;

    @Column(nullable = false)
    private Long queueNo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime activatedAt;

    private LocalDateTime expiredAt;

    private WaitingQueue(String uuid, Long concertId, Long queueNo) {
        this.uuid = uuid;
        this.concertId = concertId;
        this.queueNo = queueNo;
        this.queueStatus = INACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public static WaitingQueue createWithQueueNo(String uuid, Long concertId, Long maxQueueNo) {
        Long queueNo = maxQueueNo + 1;
        return new WaitingQueue(uuid, concertId, queueNo);
    }

    public void calculateRemainingQueueNo(Long lastActivatedQueueNo) {
        this.queueNo = queueNo - lastActivatedQueueNo;
    }

    public void activate() {
        this.queueStatus = WaitingQueueStatus.ACTIVE;
        this.activatedAt = LocalDateTime.now();
        this.expiredAt = this.activatedAt.plusMinutes(5);
    }

    public boolean isExpired() {
        return expiredAt != null && LocalDateTime.now().isAfter(expiredAt);
    }

    public void expire() {
        if (isExpired()) {
            this.queueStatus = WaitingQueueStatus.EXPIRED;
        }
    }

    public void ensureWaitingQueueIsActive() {
        if ((this.queueStatus != WaitingQueueStatus.ACTIVE) && this.expiredAt == null) {
            throw new CoreException(QUEUE_IS_INACTIVE, "uuid: "+this.uuid , "concertId: "+ this.concertId);
        }
    }

    public void ensureWaitingQueueIsNotExpired(){
        if (isExpired()) {
            throw new CoreException(QUEUE_IS_EXPIRED, "uuid: "+ this.uuid, "concertId: " + this.concertId, "expiredAt: "+ this.expiredAt);
        }
    }

    public void expireOnPaymentCompletion(){
        this.queueStatus = EXPIRED;
        this.expiredAt = LocalDateTime.now();
    }

}
