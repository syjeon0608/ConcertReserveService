package com.hhpl.concertreserve.web.application;

import com.hhpl.concertreserve.ConcertReservationServiceApplication;
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
import com.hhpl.concertreserve.app.payment.domain.PaymentInfo;
import com.hhpl.concertreserve.app.user.application.UserFacade;
import com.hhpl.concertreserve.app.user.domain.PointInfo;
import com.hhpl.concertreserve.app.user.domain.entity.Point;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import com.hhpl.concertreserve.app.user.infra.database.PointJpaRepository;
import com.hhpl.concertreserve.app.user.infra.database.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.app.user.domain.PointStatus.CHARGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ConcertReservationServiceApplication.class)
@Transactional
public class PaymentFacadeIntegrationTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private PointJpaRepository pointJpaRepository;

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Autowired
    private ScheduleJpaRepository scheduleJpaRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private Point testPoint;
    private Concert testConcert;
    private Schedule testSchedule;
    private Seat testSeat;
    private Reservation testReservation;
    private User testUser;

    @BeforeEach
    void setUp() {
        testPoint = new Point(1L, 1000);
        pointJpaRepository.save(testPoint);

        testConcert = concertJpaRepository.save(new Concert(null, "Test Concert", "Description", LocalDateTime.now(), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusDays(1)));
        testSchedule = scheduleJpaRepository.save(new Schedule(null, testConcert, LocalDateTime.now().plusDays(1), 100, 100));
        testSeat = seatJpaRepository.save(new Seat(null, testSchedule, 100, SeatStatus.AVAILABLE, LocalDateTime.now().plusDays(1),0L));
        testUser = userJpaRepository.save(new User(null,"uuid1"));
    }



    @Test
    @DirtiesContext
    @DisplayName("성공적으로 결제 처리")
    void shouldProcessPaymentSuccessfully() {
        testReservation = new Reservation(testUser, testSeat);
        reservationJpaRepository.save(testReservation);
        PaymentInfo payment = paymentFacade.processPayment(1L, 1L, "uuid1");

        assertEquals(100, payment.amount());
        assertEquals(900,testPoint.getAmount());
    }

    @Test
    @DisplayName("포인트 부족 시 예외 발생")
    void shouldThrowExceptionForInsufficientPoints() {
        assertThrows(CoreException.class, () -> {
            paymentFacade.processPayment(1L, 1L, "test-uuid2");
        });
    }

    @Test
    @DisplayName("성공적으로 포인트 충전")
    void shouldChargePointSuccessfully() {
        PointInfo updatedPoint = userFacade.chargePoint(1L, 500,CHARGE);

        assertEquals(1500, updatedPoint.amount());
    }

    @Test
    @DisplayName("잘못된 포인트 충전 금액 예외 발생")
    void shouldThrowExceptionForInvalidChargeAmount() {
        assertThrows(CoreException.class, () -> {
            userFacade.chargePoint(1L, -100, CHARGE);
        });
    }

    @Test
    @DisplayName("사용자 지갑 정보 조회")
    void shouldGetUserPointSuccessfully() {
        PointInfo point = userFacade.getUserPoint(1L);
        assertEquals(1000, point.amount());
    }
}
