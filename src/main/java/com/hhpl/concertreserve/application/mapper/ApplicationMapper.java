package com.hhpl.concertreserve.application.mapper;

import com.hhpl.concertreserve.application.model.concert.ConcertInfo;
import com.hhpl.concertreserve.application.model.concert.ReservationInfo;
import com.hhpl.concertreserve.application.model.concert.ScheduleInfo;
import com.hhpl.concertreserve.application.model.concert.SeatInfo;
import com.hhpl.concertreserve.application.model.payment.PaymentInfo;
import com.hhpl.concertreserve.application.model.user.PointInfo;
import com.hhpl.concertreserve.application.model.waitingqueue.WaitingQueueInfo;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.waitingqueue.model.WaitingQueue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationMapper {

    public static class WaitingQueueMapper {
        public static WaitingQueueInfo from(WaitingQueue waitingQueue) {
            return new WaitingQueueInfo(
                    waitingQueue.uuid(),
                    waitingQueue.queuePosition()
            );
        }
    }

    public static class ConcertMapper {
        public static List<ConcertInfo> fromConcerts(List<Concert> concerts) {
            return concerts.stream()
                    .map(ConcertMapper::toConcertInfo)
                    .toList();
        }

        public static ConcertInfo toConcertInfo(Concert concert) {
            return new ConcertInfo(
                    concert.getId(),
                    concert.getTitle(),
                    concert.getDescription(),
                    concert.getOpenDate(),
                    concert.getStartDate(),
                    concert.getEndDate()
            );
        }

        public static List<ScheduleInfo> fromSchedules(List<Schedule> schedules) {
            return schedules.stream()
                    .map(ConcertMapper::toScheduleInfo)
                    .toList();
        }

        public static ScheduleInfo toScheduleInfo(Schedule schedule) {
            return new ScheduleInfo(
                    schedule.getId(),
                    schedule.getConcert().getId(),
                    schedule.getConcertDate(),
                    schedule.getAvailableSeats(),
                    schedule.getTotalSeats()
            );
        }

        public static List<SeatInfo> fromSeats(List<Seat> seats) {
            return seats.stream()
                    .map(ConcertMapper::toSeatInfo)
                    .toList();
        }

        public static SeatInfo toSeatInfo(Seat seat) {
            return new SeatInfo(
                    seat.getId(),
                    seat.getSchedule().getId(),
                    seat.getPrice(),
                    seat.getStatus(),
                    seat.getExpiredAt()
            );
        }

        public static ReservationInfo from(Reservation reservation) {
            return new ReservationInfo(
                    reservation.getId(),
                    reservation.getUuid(),
                    reservation.getSeat().getId(),
                    reservation.getReservationStatus(),
                    reservation.getCreatedAt(),
                    reservation.getUpdatedAt()
            );
        }
    }

    public static class UserMapper {
        public static PointInfo from(Point point) {
            return new PointInfo(
                    point.getUserId(),
                    point.getAmount(),
                    point.getUpdatedAt()
            );
        }
    }


    public static class PaymentMapper {
        public static PaymentInfo from(Payment payment) {
            return new PaymentInfo(
                    payment.getReservation().getId(),
                    payment.getUserId(),
                    payment.getReservation().getSeat().getId(),
                    payment.getAmount(),
                    payment.getReservation().getReservationStatus().name(),
                    payment.getCreatedAt()
            );
        }
    }

}
