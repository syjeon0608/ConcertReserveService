package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.ConcertReservationServiceApplication;
import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.application.model.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import com.hhpl.concertreserve.infra.waitingqueue.WaitingQueueJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.hhpl.concertreserve.domain.error.ErrorType.QUEUE_IS_INACTIVE;
import static com.hhpl.concertreserve.domain.error.ErrorType.QUEUE_NOT_FOUND;
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

    @AfterEach
    void cleanUp() {
        waitingQueueJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("성공적으로 대기열에 진입")
    void shouldEnterWaitingQueueSuccessfully() {
        WaitingQueueInfo waitingQueue = waitingQueueFacade.enterWaitingQueueForConcert("550e8400-e29b-41d4-a716-446655440000", 1L);

        assertEquals(1L, waitingQueue.concertId());
        assertEquals("550e8400-e29b-41d4-a716-446655440000", waitingQueue.uuid());
    }

    @Test
    @DisplayName("유효하지 않은 콘서트 ID로 대기열 진입 시 예외 발생")
    void shouldThrowExceptionForInvalidConcertId() {
        assertThrows(CoreException.class, () -> {
            waitingQueueFacade.enterWaitingQueueForConcert("550e8400-e29b-41d4-a716-446655440000", -1L);
        });
    }

    @Test
    @DisplayName("유효하지 않은 UUID로 대기열 진입 시 예외 발생")
    void shouldThrowExceptionForInvalidUuid() {
        assertThrows(CoreException.class, () -> {
            waitingQueueFacade.enterWaitingQueueForConcert("invalid-uuid", 1L);
        });
    }

    @Test
    @DisplayName("대기열 상태 검증-아직 활성화되지 않은 대기열")
    void shouldValidateQueueStatusSuccessfully() {
        WaitingQueue savedQueue = WaitingQueue.createWithQueueNo("550e8400-e29b-41d4-a716-446655440000", 1L, 0L);
        waitingQueueJpaRepository.save(savedQueue);

        assertThrows(CoreException.class, () -> {
            waitingQueueFacade.validateQueueStatusForUser("550e8400-e29b-41d4-a716-446655440000", 1L);
        });

    }

    @Test
    @DisplayName("유효하지 않은 사용자로 대기열 상태 검증 시 예외 발생")
    void shouldThrowExceptionForInvalidUserInQueueStatus() {
        assertThrows(CoreException.class, () -> {
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
        assertEquals("INACTIVE", waitingQueueInfo.queueStatus().name());
        assertEquals(1L, waitingQueueInfo.renamingQueueNo());
    }


    @Test
    @DisplayName("대기열 조회 시 대기열에 대한 정보가 없으면 예외가 발생한다.")
    void shouldThrowExceptionWhenQueueNotFound() {
        WaitingQueue savedQueue = WaitingQueue.createWithQueueNo("550e8400-e29b-41d4-a716-446655440000", 1L, 0L);
        waitingQueueJpaRepository.save(savedQueue);

        CoreException exception = assertThrows(CoreException.class, () -> {
            waitingQueueFacade.getWaitingQueueInfoForUser("550e8400-e29b-41d4-a716-446655441111", 1L);
        });

        assertEquals(QUEUE_NOT_FOUND,exception.getErrorType());

    }

    @Test
    @DisplayName("대기열 통과 시 대기열이 활성화 되어있지 않으면 예외가 발생한다.")
    void shouldThrowExceptionWhenQueueIsInactive() {
        WaitingQueue savedQueue = WaitingQueue.createWithQueueNo("550e8400-e29b-41d4-a716-446655440000", 1L, 0L);
        waitingQueueJpaRepository.save(savedQueue);

        CoreException exception = assertThrows(CoreException.class, () -> {
            waitingQueueFacade.validateQueueStatusForUser("550e8400-e29b-41d4-a716-446655440000", 1L);
        });

        assertEquals(QUEUE_IS_INACTIVE,exception.getErrorType());

    }

}