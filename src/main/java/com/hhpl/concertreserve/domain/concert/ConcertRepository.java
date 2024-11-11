package com.hhpl.concertreserve.domain.concert;

import com.hhpl.concertreserve.domain.concert.model.*;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;

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
