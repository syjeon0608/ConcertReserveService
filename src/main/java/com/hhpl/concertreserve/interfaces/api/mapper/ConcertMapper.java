package com.hhpl.concertreserve.interfaces.api;

import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.interfaces.dto.concert.ConcertResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.ReservationResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.ScheduleResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.SeatResponse;
import org.springframework.stereotype.Component;

@Component
public class ConcertMapper {

    public ConcertResponse toConcertResponse(Concert concert) {
        return new ConcertResponse(
                concert.getId(),
                concert.getTitle(),
                concert.getDescription(),
                concert.getOpenDate(),
                concert.getStartDate(),
                concert.getEndDate()
        );
    }

    public ScheduleResponse toScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getConcert().getId(),
                schedule.getConcertDate(),
                schedule.getAvailableSeats(),
                schedule.getTotalSeats()
        );
    }

    public SeatResponse toSeatResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSchedule().getId(),
                seat.getPrice(),
                seat.getStatus(),
                seat.getExpiredAt()
        );
    }

    public ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUuid(),
                reservation.getSeat().getId(),
                reservation.getReservationStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}
