package com.hhpl.concertreserve.domain.waitingqueue;

import com.hhpl.concertreserve.domain.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.QUEUE_IS_EXPIRED;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.QUEUE_IS_INACTIVE;
import static com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueStatus.EXPIRED;
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
        assertEquals(maxQueueNo + 1, waitingQueue.getQueueNo());
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

        WaitingQueue waitingQueue3 = WaitingQueue.createWithQueueNo(uuid3, concertId2, maxQueueNo);
        assertEquals(maxQueueNo + 1, waitingQueue3.getQueueNo());

        assertEquals(11, waitingQueue1.getQueueNo());
        assertEquals(12, waitingQueue2.getQueueNo());
        assertEquals(11, waitingQueue3.getQueueNo());    //waitingQueue1 과 다른 대기열
    }

    @Test
    @DisplayName("내 대기 번호에서 마지막 활성화된 대기열 번호를 뺀 대기열 정보를 반환한다.")
    void shouldReturnWaitingQueueInfoWithRemainingQueueNo() {
        String uuid = "user-123";
        Long concertId = 1L;
        Long maxQueueNo = 10L;
        Long lastActiveQueue = 3L;
        WaitingQueue myWaitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);

        WaitingQueueInfo queueInfo = myWaitingQueue.getWaitingQueueInfo(lastActiveQueue);

        assertEquals(INACTIVE, myWaitingQueue.getQueueStatus());
        assertEquals(8L, queueInfo.renamingQueueNo());
    }

    @Test
    @DisplayName("대기열을 활성화하면 상태가 ACTIVE로 변경되고 활성화 시간과 만료 시간이 설정된다.")
    void shouldActivateWaitingQueue() {
        String uuid = "user-123";
        Long concertId = 1L;
        Long maxQueueNo = 10L;
        WaitingQueue waitingQueue =  WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);

        waitingQueue.activate();

        assertEquals(WaitingQueueStatus.ACTIVE, waitingQueue.getQueueStatus());
        assertNotNull(waitingQueue.getActivatedAt());
        assertEquals(waitingQueue.getActivatedAt().plusMinutes(5), waitingQueue.getExpiredAt());
    }

    @Test
    @DisplayName("대기열이 만료되면 상태가 EXPIRED로 변경된다.")
    void shouldExpiredWaitingQueue() {
        String uuid = "user-123";
        Long concertId = 1L;
        Long maxQueueNo = 10L;
        WaitingQueue waitingQueue =  WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);
        waitingQueue.activate();

        ReflectionTestUtils.setField(waitingQueue, "expiredAt", LocalDateTime.now().minusMinutes(1));

        waitingQueue.updateStatusIfExpired();

        assertEquals(WaitingQueueStatus.EXPIRED, waitingQueue.getQueueStatus());
    }

    @Test
    @DisplayName("토큰검증 - 아직 활성화 안된 대기열")
    void validate_shouldThrowExceptionWhenQueueIsInactive() {
        WaitingQueue waitingQueue =  WaitingQueue.createWithQueueNo("uuid", 1L, 10L);

        BusinessException exception = assertThrows(BusinessException.class, waitingQueue::validate);
        assertEquals(QUEUE_IS_INACTIVE, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰검증 - 만료된 대기열")
    void validate_shouldThrowExceptionWhenQueueIsExpired() {
        WaitingQueue waitingQueue =  WaitingQueue.createWithQueueNo("uuid", 1L, 10L);
        waitingQueue.activate();
        ReflectionTestUtils.setField(waitingQueue, "expiredAt", LocalDateTime.now().minusMinutes(1));

        waitingQueue.updateStatusIfExpired();

        BusinessException exception = assertThrows(BusinessException.class, waitingQueue::validate);
        assertEquals(QUEUE_IS_EXPIRED, exception.getErrorCode());
    }

    @Test
    @DisplayName("토큰검증 통과 - 만료되지않은 활성토큰")
    void validate_shouldPassWhenQueueIsActiveAndNotExpired() {
        WaitingQueue waitingQueue =  WaitingQueue.createWithQueueNo("uuid", 1L, 10L);
        waitingQueue.activate();

        assertDoesNotThrow(waitingQueue::validate);
    }

    @Test
    @DisplayName("결제 완료 시 활성 대기열을 만료 상태로 업데이트한다.")
    void shouldUpdateActiveQueueToExpiredAfterPayment() {
        String uuid = "user-123";
        Long concertId = 1L;
        Long maxQueueNo = 10L;
        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo(uuid, concertId, maxQueueNo);
        waitingQueue.activate();

        waitingQueue.makeExpiredWhenCompletePayment();

        assertEquals(EXPIRED, waitingQueue.getQueueStatus());
    }
}