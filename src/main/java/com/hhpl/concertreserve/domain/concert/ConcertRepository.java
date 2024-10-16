package com.hhpl.concertreserve.domain.concert;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {
    List<Concert> getAvailableConcerts(LocalDateTime now);

    List<Schedule> getSchedulesWithAvailableSeats(Long concertId, int availableSeats);

    List<Seat> getAvailableSeats(Long scheduleId, SeatStatus seatStatus);

    Seat getAvailableSelectedSeat(Long seatId);

    Seat updateSeatStatus(Seat seat);

    Reservation saveReservation(Reservation reservation);


}
