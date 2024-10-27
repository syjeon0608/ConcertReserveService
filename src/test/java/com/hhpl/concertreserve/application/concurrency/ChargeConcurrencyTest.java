package com.hhpl.concertreserve.application.concurrency;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.user.UserRepository;
import com.hhpl.concertreserve.domain.user.UserService;
import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ChargeConcurrencyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired private UserService userService;

    @Test
    @DisplayName("유저가 충전을 3번 요청하면 3번 다 반영된다.")
    public void testUserPointUpdateConcurrentRequests() throws InterruptedException {

        Point point = new Point(1L,20000);
        userRepository.updatePoint(point);

        int numberOfThreads = 3;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    userService.updateUserPoint(1L, 10000, PointStatus.CHARGE);
                    successfulRegistrations[0]++;
                } catch (CoreException e) {
                    failedRegistrations[0]++;
                    System.err.println("Error for user " + Thread.currentThread().getName() + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Point updatedPoint = userService.getUserPoint(1L);

        assertEquals(3, successfulRegistrations[0]);
        assertEquals(50000,updatedPoint.getAmount());

    }
}
