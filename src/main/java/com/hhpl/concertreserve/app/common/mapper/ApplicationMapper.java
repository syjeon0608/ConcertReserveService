package com.hhpl.concertreserve.app.common.mapper;

import com.hhpl.concertreserve.app.concert.domain.ConcertInfo;
import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import com.hhpl.concertreserve.app.concert.domain.ScheduleInfo;
import com.hhpl.concertreserve.app.concert.domain.SeatInfo;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.payment.domain.PaymentInfo;
import com.hhpl.concertreserve.app.payment.domain.entity.Payment;
import com.hhpl.concertreserve.app.user.domain.PointInfo;
import com.hhpl.concertreserve.app.user.domain.entity.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationMapper {

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
                    reservation.getUser().getId(),
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
