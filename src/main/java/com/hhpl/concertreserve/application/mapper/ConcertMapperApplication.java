package com.hhpl.concertreserve.application.mapper;

import com.hhpl.concertreserve.application.model.ConcertInfo;
import com.hhpl.concertreserve.application.model.ReservationInfo;
import com.hhpl.concertreserve.application.model.ScheduleInfo;
import com.hhpl.concertreserve.application.model.SeatInfo;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertMapperApplication {

    public List<ConcertInfo> fromConcerts(List<Concert> concerts) {
        return concerts.stream()
                .map(this::toConcertInfo)
                .toList();
    }

    public ConcertInfo toConcertInfo(Concert concert) {
        return new ConcertInfo(
                concert.getId(),
                concert.getTitle(),
                concert.getDescription(),
                concert.getOpenDate(),
                concert.getStartDate(),
                concert.getEndDate()
        );
    }

    public List<ScheduleInfo> fromSchedules(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::toScheduleInfo)
                .toList();
    }

    public ScheduleInfo toScheduleInfo(Schedule schedule) {
        return new ScheduleInfo(
                schedule.getId(),
                schedule.getConcert().getId(),
                schedule.getConcertDate(),
                schedule.getAvailableSeats(),
                schedule.getTotalSeats()
        );
    }

    public List<SeatInfo> fromSeats(List<Seat> seats) {
        return seats.stream()
                .map(this::toSeatInfo)
                .toList();
    }

    public SeatInfo toSeatInfo(Seat seat) {
        return new SeatInfo(
                seat.getId(),
                seat.getSchedule().getId(),
                seat.getPrice(),
                seat.getStatus(),
                seat.getExpiredAt()
        );
    }


    public ReservationInfo from(Reservation reservation) {
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
