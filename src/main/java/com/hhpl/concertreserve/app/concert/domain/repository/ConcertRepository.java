package com.hhpl.concertreserve.app.concert.domain.repository;

import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {
    List<Concert> getAvailableConcerts(LocalDateTime now);

    List<Schedule> getSchedulesWithAvailableSeatCount(Long concertId);

    List<Seat> getAvailableSeats(Long scheduleId, SeatStatus seatStatus);

    Seat getSelectedSeat(Long seatId);

    void save(Seat seat);

    Reservation save(Reservation reservation);

    Reservation getMyReservation(Long reservationId);

    List<Seat> findExpiredSeatsToBeAvailable(LocalDateTime now);

    Reservation findBySeatId(Long id);

}
