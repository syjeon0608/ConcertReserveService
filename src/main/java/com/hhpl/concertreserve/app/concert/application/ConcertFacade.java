package com.hhpl.concertreserve.app.concert.application;

import com.hhpl.concertreserve.app.common.mapper.ApplicationMapper;
import com.hhpl.concertreserve.app.concert.domain.ConcertInfo;
import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import com.hhpl.concertreserve.app.concert.domain.ScheduleInfo;
import com.hhpl.concertreserve.app.concert.domain.SeatInfo;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.concert.domain.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    public List<ConcertInfo> getAvailableConcerts() {
        List<Concert> concerts = concertService.getAvailableConcerts();
        return ApplicationMapper.ConcertMapper.fromConcerts(concerts);
    }

    public List<ScheduleInfo> getAvailableSchedules(Long concertId) {
        List<Schedule> schedules = concertService.getAvailableSchedules(concertId);
        return ApplicationMapper.ConcertMapper.fromSchedules(schedules);
    }

    public List<SeatInfo> getAvailableSeats(Long scheduleId) {
        List<Seat> seats = concertService.getAvailableSeats(scheduleId);
        return ApplicationMapper.ConcertMapper.fromSeats(seats);
    }

    public ReservationInfo createTemporarySeatReservation(String uuid, Long seatId) {
        Reservation reservation = concertService.createReservation(uuid, seatId);
        return ApplicationMapper.ConcertMapper.from(reservation);
    }

}
