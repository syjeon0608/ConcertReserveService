package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ConcertMapperApplication;
import com.hhpl.concertreserve.application.model.ConcertInfo;
import com.hhpl.concertreserve.application.model.ReservationInfo;
import com.hhpl.concertreserve.application.model.ScheduleInfo;
import com.hhpl.concertreserve.application.model.SeatInfo;
import com.hhpl.concertreserve.domain.concert.*;
import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final ConcertMapperApplication concertMapper;

    public List<ConcertInfo> getAvailableConcerts() {
        List<Concert> concerts = concertService.getAvailableConcerts();
        return concertMapper.fromConcerts(concerts);
    }

    public List<ScheduleInfo> getAvailableSchedules(Long concertId) {
        List<Schedule> schedules = concertService.getAvailableSchedules(concertId);
        return concertMapper.fromSchedules(schedules);
    }

    public List<SeatInfo> getAvailableSeats(Long scheduleId) {
        List<Seat> seats = concertService.getAvailableSeats(scheduleId);
        return concertMapper.fromSeats(seats);
    }

    @Transactional
    public ReservationInfo createTemporarySeatReservation(String uuid, Long seatId) {
        concertService.reserveSeatTemporarily(seatId);
        Reservation reservation = concertService.createReservation(uuid, seatId);
        return concertMapper.from(reservation);
    }

}
