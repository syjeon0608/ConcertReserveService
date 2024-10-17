package com.hhpl.concertreserve.domain.waitingqueue;

import com.hhpl.concertreserve.domain.error.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.QUEUE_IS_EXPIRED;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.QUEUE_IS_INACTIVE;
import static com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueStatus.EXPIRED;
import static com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueStatus.INACTIVE;

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

    public Long calculateCurrentQueueNo(Long lastActivatedQueueNo) {
        return this.queueNo - lastActivatedQueueNo;
    }


    public WaitingQueueInfo getRecentWaitingQueueInfo(Long lastActivatedQueueNo) {
        Long remainingQueueNo = calculateCurrentQueueNo(lastActivatedQueueNo);
        return new WaitingQueueInfo(
                this.concertId,
                this.queueStatus.name(),
                remainingQueueNo
        );
    }

    public void activate() {
        this.queueStatus = WaitingQueueStatus.ACTIVE;
        this.activatedAt = LocalDateTime.now();
        this.expiredAt = this.activatedAt.plusMinutes(5);
    }


    public boolean isExpired() {
        return expiredAt != null && LocalDateTime.now().isAfter(expiredAt);
    }

    public void updateStatusIfExpired() {
        if (isExpired()) {
            this.queueStatus = WaitingQueueStatus.EXPIRED;
        }
    }

    public void validate() {
        if ((this.queueStatus != WaitingQueueStatus.ACTIVE) && this.expiredAt == null) {
            throw new BusinessException(QUEUE_IS_INACTIVE);
        }

        if (isExpired()) {
            throw new BusinessException(QUEUE_IS_EXPIRED);
        }
    }

    public void expireOnPaymentCompletion(){
        this.queueStatus = EXPIRED;
        this.expiredAt = LocalDateTime.now();
    }


}
