package com.hhpl.concertreserve.app.common.mapper;

import com.hhpl.concertreserve.app.concert.api.dto.ConcertResponse;
import com.hhpl.concertreserve.app.concert.api.dto.ReservationResponse;
import com.hhpl.concertreserve.app.concert.api.dto.ScheduleResponse;
import com.hhpl.concertreserve.app.concert.api.dto.SeatResponse;
import com.hhpl.concertreserve.app.concert.domain.ConcertInfo;
import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import com.hhpl.concertreserve.app.concert.domain.ScheduleInfo;
import com.hhpl.concertreserve.app.concert.domain.SeatInfo;
import com.hhpl.concertreserve.app.payment.api.dto.ReservationPaymentResponse;
import com.hhpl.concertreserve.app.payment.domain.PaymentInfo;
import com.hhpl.concertreserve.app.user.api.dto.PointAmountResponse;
import com.hhpl.concertreserve.app.user.domain.PointInfo;
import com.hhpl.concertreserve.app.waitingqueue.api.dto.WaitingQueueResponse;
import com.hhpl.concertreserve.app.waitingqueue.domain.WaitingQueue;
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
                    reservation.userId(),
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
        public static WaitingQueueResponse toResponse(WaitingQueue waitingQueueInfo) {
            return new WaitingQueueResponse(
                    waitingQueueInfo.uuid(),
                    waitingQueueInfo.queuePosition()
            );
        }
    }

}
