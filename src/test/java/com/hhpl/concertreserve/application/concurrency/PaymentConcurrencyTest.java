package com.hhpl.concertreserve.application.concurrency;

import com.hhpl.concertreserve.application.facade.PaymentFacade;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.payment.PaymentService;
import com.hhpl.concertreserve.domain.user.UserRepository;
import com.hhpl.concertreserve.domain.user.UserService;
import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
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

    @Autowired
    PaymentFacade paymentFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;



    @Test
    @DisplayName("사용자가 5번 결제 요청하면 1번만 성공한다.")
    void testConcurrentPaymentSuccessOnReservation() throws InterruptedException {

        Point point = new Point(9L,30000);
        userRepository.updatePoint(point);
        Concert  concert = concertJpaRepository.save(new Concert(5L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule  schedule = scheduleJpaRepository.save(new Schedule(5L, concert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat seat = seatJpaRepository.save(new Seat(5L, schedule, 10000, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),10L));
        seatJpaRepository.save(seat);
        Reservation reservation = new Reservation("uuid-123", seat);
        reservationJpaRepository.save(reservation);

        int numberOfThreads = 5;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.processPayment(reservation.getId(),point.getUserId(),reservation.getUuid() );
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

        assertEquals(20000,updatedPoint.getAmount());
        assertEquals(1, successfulRegistrations[0], "한번만 결제에 성공해야한다");
        assertEquals(4, failedRegistrations[0], "나머지 결제는 다 실패");

    }


    @Test
    @DisplayName("사용자가 충전 후 결제를 하면 금액이 모두 정확하게 반영된다")
    void testConcurrentChargeAndUse() throws InterruptedException {
        Point point = new Point(10L,50000);
        userRepository.updatePoint(point);
        Concert  concert = concertJpaRepository.save(new Concert(5L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule  schedule = scheduleJpaRepository.save(new Schedule(2L, concert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat seat = seatJpaRepository.save(new Seat(3L, schedule, 80000, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),10L));
        seatJpaRepository.save(seat);
        Reservation reservation = new Reservation("uuid-777", seat);
        reservationJpaRepository.save(reservation);

        int numberOfThreads = 1;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    userService.updateUserPoint(point.getUserId(), 100000, PointStatus.CHARGE);
                    paymentFacade.processPayment(reservation.getId(),point.getUserId(),reservation.getUuid());
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

        assertEquals(70000,updatedPoint.getAmount());

    }
}
