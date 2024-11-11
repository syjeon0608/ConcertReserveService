package com.hhpl.concertreserve.application.concurrency;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.user.UserRepository;
import com.hhpl.concertreserve.domain.user.UserService;
import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import com.hhpl.concertreserve.domain.user.model.User;
import com.hhpl.concertreserve.infra.database.user.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PointConcurrencyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("유저가 충전을 3번 요청하면 3번 다 반영된다.")
    public void testUserPointUpdateConcurrentRequests() throws InterruptedException {
        User user = new User(null,"uuid1");
        userJpaRepository.save(user);
        Point point = new Point(user.getId(),20000);
        userRepository.updatePoint(point);

        int numberOfThreads = 3;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    userService.updateUserPoint(point.getUserId(), 10000, PointStatus.CHARGE);
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

        Point updatedPoint = userService.getUserPoint(point.getUserId());

        assertEquals(3, successfulRegistrations[0]);
        assertEquals(50000,updatedPoint.getAmount());

    }


    @Test
    @DisplayName("유저가 동시에 5번의 포인트 사용 요청을 보내면 5번 다 반영된다.")
    public void testUserPointUpdateConcurrentRequests2() throws InterruptedException {
        User user = new User(null,"uuid1");
        userJpaRepository.save(user);
        Point point = new Point(user.getId(),200000);
        userRepository.updatePoint(point);

        int numberOfThreads = 5;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    userService.updateUserPoint(point.getUserId(), 10000, PointStatus.USE);
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

        Point updatedPoint = userService.getUserPoint(point.getUserId());

        assertEquals(5, successfulRegistrations[0]);
        assertEquals(150000,updatedPoint.getAmount());

    }
}
