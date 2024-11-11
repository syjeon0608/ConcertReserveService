package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.application.model.concert.ConcertInfo;
import com.hhpl.concertreserve.application.model.concert.ReservationInfo;
import com.hhpl.concertreserve.application.model.concert.ScheduleInfo;
import com.hhpl.concertreserve.application.model.concert.SeatInfo;
import com.hhpl.concertreserve.application.model.payment.PaymentInfo;
import com.hhpl.concertreserve.application.model.user.PointInfo;
import com.hhpl.concertreserve.application.model.waitingqueue.WaitingQueueInfo;
import com.hhpl.concertreserve.interfaces.dto.concert.ConcertResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.ReservationResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.ScheduleResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.SeatResponse;
import com.hhpl.concertreserve.interfaces.dto.payment.PointAmountResponse;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentResponse;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControllerMapper {

    public static class ConcertMapper {
        public static List<ConcertResponse> toConcertResponseList(List<ConcertInfo> concerts) {
            return concerts.stream()
                    .map(ConcertMapper::toConcertResponse)
                    .toList();
        }

        public static ConcertResponse toConcertResponse(ConcertInfo concert) {
            return new ConcertResponse(
                    concert.id(),
                    concert.title(),
                    concert.description(),
                    concert.openDate(),
                    concert.startDate(),
                    concert.endDate()
            );
        }

        public static List<ScheduleResponse> toScheduleResponseList(List<ScheduleInfo> schedules) {
            return schedules.stream()
                    .map(ConcertMapper::toScheduleResponse)
                    .toList();
        }

        public static ScheduleResponse toScheduleResponse(ScheduleInfo schedule) {
            return new ScheduleResponse(
                    schedule.id(),
                    schedule.concertId(),
                    schedule.concertDate(),
                    schedule.availableSeats(),
                    schedule.totalSeats()
            );
        }

        public static List<SeatResponse> toSeatResponseList(List<SeatInfo> seats) {
            return seats.stream()
                    .map(ConcertMapper::toSeatResponse)
                    .toList();
        }

        public static SeatResponse toSeatResponse(SeatInfo seat) {
            return new SeatResponse(
                    seat.id(),
                    seat.scheduleId(),
                    seat.price(),
                    seat.status(),
                    seat.expiredAt()
            );
        }

        public static ReservationResponse toReservationResponse(ReservationInfo reservation) {
            return new ReservationResponse(
                    reservation.id(),
                    reservation.user(),
                    reservation.seatId(),
                    reservation.status(),
                    reservation.createdAt(),
                    reservation.updatedAt()
            );
        }
    }

    public static class PaymentMapper {
        public static ReservationPaymentResponse toResponse(PaymentInfo paymentInfo) {
            return new ReservationPaymentResponse(
                    paymentInfo.reservationId(),
                    paymentInfo.userId(),
                    paymentInfo.seatId(),
                    paymentInfo.amount(),
                    paymentInfo.reservationStatus(),
                    paymentInfo.paymentDate()
            );
        }
    }

    public static class UserMapper {
        public static PointAmountResponse toResponse(PointInfo point) {
            return new PointAmountResponse(
                    point.userId(),
                    point.amount(),
                    point.updatedAt()
            );
        }
    }

    public static class WaitingQueueMapper {
        public static WaitingQueueResponse toResponse(WaitingQueueInfo waitingQueueInfo) {
            return new WaitingQueueResponse(
                    waitingQueueInfo.uuid(),
                    waitingQueueInfo.queuePosition()
            );
        }
    }

}
