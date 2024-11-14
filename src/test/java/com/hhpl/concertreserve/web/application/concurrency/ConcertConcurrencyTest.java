package com.hhpl.concertreserve.web.application.concurrency;

import com.hhpl.concertreserve.app.ConcertReservationServiceApplication;
import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.concert.domain.service.ConcertService;
import com.hhpl.concertreserve.app.concert.infra.database.ConcertJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.ScheduleJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.SeatJpaRepository;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import com.hhpl.concertreserve.app.user.infra.database.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConcertReservationServiceApplication.class)
public class ConcertConcurrencyTest {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private ConcertService concertService;


    @Test
    @DisplayName("하나의 좌석에 대해 100명이 동시에 예약신청을 하면 한명만 성공하고 나머지는 실패한다.")
    public void testCreateSeatReservationConcurrentAccess() throws InterruptedException {
        Concert  testConcert = concertJpaRepository.save(new Concert(1L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule  testSchedule = scheduleJpaRepository.save(new Schedule(1L, testConcert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat testSeat = seatJpaRepository.save(new Seat(1L, testSchedule, 100, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),0L));
        seatJpaRepository.save(testSeat);

        int numberOfThreads = 100;
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        Runnable task = () -> {
            try {
                latch.await();
                String uniqueUuid = "uuid-" + Thread.currentThread().getName();
                userJpaRepository.save(new User(null,uniqueUuid));
                concertService.createReservation(uniqueUuid, testSeat.getId());
            } catch (Exception e) {
                exceptions.add(e);
                System.err.println(e.getMessage());
            }
        };

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(task);
        }

        latch.countDown();

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(99, exceptions.size());
    }

}
