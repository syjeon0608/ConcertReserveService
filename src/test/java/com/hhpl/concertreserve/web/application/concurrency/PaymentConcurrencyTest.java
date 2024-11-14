package com.hhpl.concertreserve.web.application.concurrency;

import com.hhpl.concertreserve.app.ConcertReservationServiceApplication;
import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.concert.infra.database.ConcertJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.ReservationJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.ScheduleJpaRepository;
import com.hhpl.concertreserve.app.concert.infra.database.SeatJpaRepository;
import com.hhpl.concertreserve.app.payment.application.PaymentFacade;
import com.hhpl.concertreserve.app.payment.domain.service.PaymentService;
import com.hhpl.concertreserve.app.payment.infra.database.PaymentJpaRepository;
import com.hhpl.concertreserve.app.user.domain.PointStatus;
import com.hhpl.concertreserve.app.user.domain.entity.Point;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import com.hhpl.concertreserve.app.user.domain.repository.UserRepository;
import com.hhpl.concertreserve.app.user.domain.service.UserService;
import com.hhpl.concertreserve.app.user.infra.database.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConcertReservationServiceApplication.class)
public class PaymentConcurrencyTest {

    @Autowired
    private UserJpaRepository userJpaRepository;
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
        User user = new User(null,"uuid1");
        userJpaRepository.save(user);
        Point point = new Point(user.getId(),30000);
        userRepository.updatePoint(point);

        Concert concert = concertJpaRepository.save(new Concert(5L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule schedule = scheduleJpaRepository.save(new Schedule(5L, concert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat seat = seatJpaRepository.save(new Seat(5L, schedule, 10000, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),10L));
        seatJpaRepository.save(seat);
        Reservation reservation = new Reservation(user, seat);
        reservationJpaRepository.save(reservation);

        int numberOfThreads = 5;
        int[] successfulRegistrations = {0};
        int[] failedRegistrations = {0};

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    paymentFacade.processPayment(reservation.getId(),point.getUserId(), user.getUuid());
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
        User user = new User(null,"uuid2");
        userJpaRepository.save(user);
        Point point = new Point(user.getId(),30000);
        userRepository.updatePoint(point);

        Concert  concert = concertJpaRepository.save(new Concert(6L, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        Schedule  schedule = scheduleJpaRepository.save(new Schedule(6L, concert, LocalDateTime.now().plusDays(1), 100, 100));
        Seat seat = seatJpaRepository.save(new Seat(50L, schedule, 60000, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),10L));
        seatJpaRepository.save(seat);
        Reservation reservation = new Reservation(user, seat);
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
                    paymentFacade.processPayment(reservation.getId(),point.getUserId(),user.getUuid());
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
