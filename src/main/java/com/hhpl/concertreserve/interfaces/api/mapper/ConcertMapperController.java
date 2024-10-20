package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.application.model.ConcertInfo;
import com.hhpl.concertreserve.application.model.ReservationInfo;
import com.hhpl.concertreserve.application.model.ScheduleInfo;
import com.hhpl.concertreserve.application.model.SeatInfo;
import com.hhpl.concertreserve.interfaces.dto.concert.ConcertResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.ReservationResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.ScheduleResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.SeatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertMapperController {

    public List<ConcertResponse> toConcertResponseList(List<ConcertInfo> concerts) {
        return concerts.stream()
                .map(this::toConcertResponse)
                .toList();
    }

    public ConcertResponse toConcertResponse(ConcertInfo concert) {
        return new ConcertResponse(
                concert.id(),
                concert.title(),
                concert.description(),
                concert.openDate(),
                concert.startDate(),
                concert.endDate()
        );
    }

    public List<ScheduleResponse> toScheduleResponseList(List<ScheduleInfo> schedules) {
        return schedules.stream()
                .map(this::toScheduleResponse)
                .toList();
    }

    public ScheduleResponse toScheduleResponse(ScheduleInfo schedule) {
        return new ScheduleResponse(
                schedule.id(),
                schedule.concertId(),
                schedule.concertDate(),
                schedule.availableSeats(),
                schedule.totalSeats()
        );
    }

    public List<SeatResponse> toSeatResponseList(List<SeatInfo> seats) {
        return seats.stream()
                .map(this::toSeatResponse)
                .toList();
    }

    public SeatResponse toSeatResponse(SeatInfo seat) {
        return new SeatResponse(
                seat.id(),
                seat.scheduleId(),
                seat.price(),
                seat.status(),
                seat.expiredAt()
        );
    }

    public ReservationResponse toReservationResponse(ReservationInfo reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.uuid(),
                reservation.seatId(),
                reservation.status(),
                reservation.createdAt(),
                reservation.updatedAt()
        );
    }
}
