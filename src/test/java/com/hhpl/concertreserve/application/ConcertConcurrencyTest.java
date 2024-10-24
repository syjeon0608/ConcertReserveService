package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.domain.concert.ConcertService;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.infra.database.concert.ConcertJpaRepository;
import com.hhpl.concertreserve.infra.database.concert.ScheduleJpaRepository;
import com.hhpl.concertreserve.infra.database.concert.SeatJpaRepository;
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

@SpringBootTest
public class ConcertConcurrencyTest {

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private ConcertService concertService;



    @Test
    public void testCreateReservationConcurrentAccess() throws InterruptedException {
        Concert  testConcert = concertJpaRepository.save(new Concert(1L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule  testSchedule = scheduleJpaRepository.save(new Schedule(1L, testConcert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat testSeat = seatJpaRepository.save(new Seat(1L, testSchedule, 100, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),0L));
        seatJpaRepository.save(testSeat);

        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        Runnable task = () -> {
            try {
                latch.await();
                String uniqueUuid = "uuid-" + Thread.currentThread().getName();
                concertService.createReservation(uniqueUuid, testSeat.getId());
            } catch (Exception e) {
                exceptions.add(e);
                String uniqueUuid = "uuid-" + Thread.currentThread().getName();
                System.err.println("Thread Exception: " + e.getMessage());
                System.err.println("UUID: " + uniqueUuid);
            }
        };

        for (int i = 0; i < 5; i++) {
            executorService.execute(task);
        }

        latch.countDown();

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(4, exceptions.size());
    }

}
