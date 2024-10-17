package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.ConcertReservationServiceApplication;
import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueueInfo;
import com.hhpl.concertreserve.infra.waitingqueue.WaitingQueueJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ConcertReservationServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class WaitingQueueFacadeIntegrationTest {
    @Autowired
    private WaitingQueueFacade waitingQueueFacade;

    @Autowired
    private WaitingQueueJpaRepository waitingQueueJpaRepository;


    @Test
    @DisplayName("성공적으로 대기열에 진입")
    void shouldEnterWaitingQueueSuccessfully() {
        WaitingQueue waitingQueue = waitingQueueFacade.enterWaitingQueueForConcert("550e8400-e29b-41d4-a716-446655440000", 1L);

        assertEquals(1L, waitingQueue.getConcertId());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", waitingQueue.getUuid());
    }

    @Test
    @DisplayName("유효하지 않은 콘서트 ID로 대기열 진입 시 예외 발생")
    void shouldThrowExceptionForInvalidConcertId() {
        assertThrows(BusinessException.class, () -> {
            waitingQueueFacade.enterWaitingQueueForConcert("550e8400-e29b-41d4-a716-446655440000", -1L);
        });
    }

    @Test
    @DisplayName("유효하지 않은 UUID로 대기열 진입 시 예외 발생")
    void shouldThrowExceptionForInvalidUuid() {
        assertThrows(BusinessException.class, () -> {
            waitingQueueFacade.enterWaitingQueueForConcert("invalid-uuid", 1L);
        });
    }

    @Test
    @DisplayName("대기열 상태 검증-아직 활성화되지 않은 대기열")
    void shouldValidateQueueStatusSuccessfully() {
        WaitingQueue savedQueue = WaitingQueue.createWithQueueNo("550e8400-e29b-41d4-a716-446655440000", 1L, 0L);
        waitingQueueJpaRepository.save(savedQueue);

        assertThrows(BusinessException.class, () -> {
            waitingQueueFacade.validateQueueStatusForUser("550e8400-e29b-41d4-a716-446655440000", 1L);
        });

    }

    @Test
    @DisplayName("유효하지 않은 사용자로 대기열 상태 검증 시 예외 발생")
    void shouldThrowExceptionForInvalidUserInQueueStatus() {
        assertThrows(BusinessException.class, () -> {
            waitingQueueFacade.validateQueueStatusForUser("invalid-uuid", 1L);
        });
    }

    @Test
    @DisplayName("대기열 정보 조회 성공")
    void shouldGetWaitingQueueInfoSuccessfully() {
        WaitingQueue savedQueue = WaitingQueue.createWithQueueNo("550e8400-e29b-41d4-a716-446655440000", 1L, 0L);
        waitingQueueJpaRepository.save(savedQueue);

        WaitingQueueInfo waitingQueueInfo = waitingQueueFacade.getWaitingQueueInfoForUser("550e8400-e29b-41d4-a716-446655440000", 1L);

        assertEquals(1L, waitingQueueInfo.concertId());
        assertEquals("INACTIVE", waitingQueueInfo.queueStatus());
        assertEquals(1L, waitingQueueInfo.renamingQueueNo());
    }

}