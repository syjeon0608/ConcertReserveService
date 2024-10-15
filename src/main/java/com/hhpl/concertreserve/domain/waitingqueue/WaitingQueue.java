package com.hhpl.concertreserve.domain.waitingqueue;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

}
