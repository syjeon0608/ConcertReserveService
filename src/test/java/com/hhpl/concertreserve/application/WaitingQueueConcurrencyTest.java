package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.domain.waitingqueue.WaitingQueueService;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import com.hhpl.concertreserve.infra.database.waitingqueue.WaitingQueueJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WaitingQueueConcurrencyTest {

    @Autowired
    private WaitingQueueService waitingQueueService;
    @Autowired
    private WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Test
    @DisplayName("여러명의 사용자가 동시에 대기열 생성을 요청하면 큐 번호를 순차적으로 줘야 한다.")
    public void shouldHandleConcurrentCreateWaitingQueue() throws InterruptedException {
        int numberOfThreads = 50;
        List<Long> queueNumbers = Collections.synchronizedList(new ArrayList<>());

        WaitingQueue waitingQueue = WaitingQueue.createWithQueueNo("uuid1", 1L, 0L);
        waitingQueueJpaRepository.save(waitingQueue);

        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    WaitingQueue queue = waitingQueueService.enterWaitingQueue(Thread.currentThread().getName(), 1L);
                    queueNumbers.add(queue.getQueueNo());
                    successfulRegistrations[0]++;
                    System.out.println(Thread.currentThread().getName() + " assigned queue number: " + queue.getQueueNo());

                } catch (Exception e) {
                    failedRegistrations[0]++;
                    System.err.println("Error for user " + Thread.currentThread().getName() + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        assertEquals(50, successfulRegistrations[0], "모든 사용자가 성공적으로 등록되어야 한다.");
        assertEquals(0, failedRegistrations[0], "실패한 등록이 없어야 한다.");

        Set<Long> uniqueQueueNumbers = new HashSet<>(queueNumbers);
        assertEquals(uniqueQueueNumbers.size(), queueNumbers.size(), "큐 번호에 중복이 있어서는 안 된다.");
    }

}
