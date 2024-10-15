package com.hhpl.concertreserve.domain.waitingqueue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueStatus.INACTIVE;
import static org.junit.jupiter.api.Assertions.*;

class WaitingQueueTest {

    @Test
    @DisplayName("마지막 대기열 번호에 1을 더한 번호로 대기열을 생성한다.")
    public void shouldCreateWaitingQueueWithIncrementedQueueNo() {

        String uuid = "user-123";
        Long concertId = 1L;
        Long maxQueueNo = 10L;
        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);

        assertNull(waitingQueue.getId());
        assertEquals(INACTIVE, waitingQueue.getQueueStatus());
        assertEquals(uuid, waitingQueue.getUuid());
        assertEquals(concertId, waitingQueue.getConcertId());
        assertEquals(maxQueueNo+1, waitingQueue.getQueueNo());
        assertNotNull(waitingQueue.getCreatedAt());
        assertNull(waitingQueue.getActivatedAt());
        assertNull(waitingQueue.getExpiredAt());

    }

    @Test
    @DisplayName("각 콘서트에 대해 고유한 대기열 번호를 생성한다.")
    public void shouldCreateUniqueQueueNoForEachConcert() {
        String uuid1 = "user-123";
        String uuid2 = "user-124";
        Long concertId1 = 1L;
        Long maxQueueNo = 10L;

        WaitingQueue waitingQueue1 = WaitingQueue.createWithQueueNo(uuid1, concertId1, maxQueueNo);
        assertEquals(maxQueueNo + 1, waitingQueue1.getQueueNo());

        WaitingQueue waitingQueue2 = WaitingQueue.createWithQueueNo(uuid2, concertId1, waitingQueue1.getQueueNo());
        assertEquals(waitingQueue1.getQueueNo() + 1, waitingQueue2.getQueueNo());

        //다른 콘서트의 대기열
        String uuid3 = "user-125";
        Long concertId2 = 2L;

        WaitingQueue waitingQueue3 =  WaitingQueue.createWithQueueNo(uuid3, concertId2, maxQueueNo);
        assertEquals(maxQueueNo + 1, waitingQueue3.getQueueNo());

        assertEquals(11,waitingQueue1.getQueueNo());
        assertEquals(12,waitingQueue2.getQueueNo());
        assertEquals(11,waitingQueue3.getQueueNo());    //waitingQueue1 과 다른 대기열
    }

}