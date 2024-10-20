package com.hhpl.concertreserve.application.facade;

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

    public List<Concert> getAvailableConcerts() {
        return concertService.getAvailableConcerts();
    }

    public List<Schedule> getAvailableSchedules(Long concertId) {
        return concertService.getAvailableSchedules(concertId);
    }

    public List<Seat> getAvailableSeats(Long scheduleId) {
        return concertService.getAvailableSeats(scheduleId);
    }

    @Transactional
    public Reservation createTemporarySeatReservation(String uuid, Long seatId) {
        concertService.reserveSeatTemporarily(seatId);
        return concertService.createReservation(uuid, seatId);
    }

}
