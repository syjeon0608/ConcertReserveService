package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ApplicationMapper;
import com.hhpl.concertreserve.application.model.concert.ConcertInfo;
import com.hhpl.concertreserve.application.model.concert.ReservationInfo;
import com.hhpl.concertreserve.application.model.concert.ScheduleInfo;
import com.hhpl.concertreserve.application.model.concert.SeatInfo;
import com.hhpl.concertreserve.domain.concert.ConcertService;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
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
