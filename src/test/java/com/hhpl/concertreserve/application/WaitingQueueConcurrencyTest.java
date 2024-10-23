package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WaitingQueueConcurrencyTest {

    @Autowired
    private WaitingQueueService waitingQueueService;

    @Test
    @DisplayName("여러명의 사용자가 동시에 대기열 생성을 요청하면 큐 번호를 순차적으로 줘야 한다.")
    public void shouldHandleConcurrentCreateWaitingQueue() throws InterruptedException {

        int numberOfThreads = 50;
        List<Long> queueNumbers = Collections.synchronizedList(new ArrayList<>());

        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final String userId = "" + (i + 1);  // 사용자 ID를 문자열로 생성

            executorService.submit(() -> {
                try {
                    // 대기열에 사용자 등록
                    WaitingQueue queue = waitingQueueService.enterWaitingQueue(userId, 1L);
                    // 생성된 queueNo를 리스트에 추가
                    queueNumbers.add(queue.getQueueNo());
                    successfulRegistrations[0]++;
                } catch (Exception e) {
                    failedRegistrations[0]++;
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 모든 스레드가 작업을 끝날 때까지 대기
        executorService.shutdown();

        // queueNumbers 리스트에 저장된 큐 번호가 순차적인지 확인
        List<Long> sortedQueueNumbers = new ArrayList<>(queueNumbers);
        Collections.sort(sortedQueueNumbers);
        assertEquals(queueNumbers, sortedQueueNumbers, "큐 번호는 순차적으로 부여되어야 한다.");

        // 모든 스레드가 성공적으로 처리되었는지 확인
        assertEquals(5, successfulRegistrations[0], "모든 사용자가 성공적으로 등록되어야 한다.");
        assertEquals(0, failedRegistrations[0], "실패한 등록이 없어야 한다.");
    }
}