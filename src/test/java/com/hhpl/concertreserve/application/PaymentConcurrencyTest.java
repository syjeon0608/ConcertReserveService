package com.hhpl.concertreserve.application;

import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.payment.PaymentService;
import com.hhpl.concertreserve.infra.database.concert.ConcertJpaRepository;
import com.hhpl.concertreserve.infra.database.concert.ReservationJpaRepository;
import com.hhpl.concertreserve.infra.database.concert.ScheduleJpaRepository;
import com.hhpl.concertreserve.infra.database.concert.SeatJpaRepository;
import com.hhpl.concertreserve.infra.database.payment.PaymentJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaymentConcurrencyTest {

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    PaymentJpaRepository paymentJpaRepository;

    @Autowired
    ReservationJpaRepository reservationJpaRepository;

    @Autowired
    PaymentService paymentService;

    @Test
    @DisplayName("사용자가 5번 결제 요청하면 1번만 성공한다.")
    void testConcurrentPaymentSuccessOnReservation() throws InterruptedException {

        Concert  concert = concertJpaRepository.save(new Concert(1L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule  schedule = scheduleJpaRepository.save(new Schedule(1L, concert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat seat = seatJpaRepository.save(new Seat(1L, schedule, 100, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),0L));
        seatJpaRepository.save(seat);
        Reservation reservation = new Reservation("uuid", seat);
        reservationJpaRepository.save(reservation);

        int numberOfThreads = 5;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    paymentService.completePayment(1L,reservation,100 );
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

        assertEquals(1, successfulRegistrations[0], "한번만 결제에 성공해야한다");
        assertEquals(4, failedRegistrations[0], "나머지 결제는 다 실패");

    }
}
