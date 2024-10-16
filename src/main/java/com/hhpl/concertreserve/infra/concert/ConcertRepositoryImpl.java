package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final SeatJpaRepository seatJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public List<Concert> getAvailableConcerts(LocalDateTime now) {
        return concertJpaRepository.getAvailableConcerts(now);
    }

    @Override
    public List<Schedule> getSchedulesWithAvailableSeats(Long concertId, int availableSeats) {
        return scheduleJpaRepository.findByConcertIdAndAvailableSeatsGreaterThan(concertId,availableSeats);
    }

    @Override
    public List<Seat> getAvailableSeats(Long scheduleId, SeatStatus seatStatus) {
        return seatJpaRepository.findByScheduleIdAndStatus(scheduleId,seatStatus);
    }

    @Override
    public Seat getAvailableSelectedSeat(Long seatId) {
        return seatJpaRepository.getAvailableSelectedSeat(seatId);
    }

    @Override
    public Seat updateSeatStatus(Seat seat) {
        return seatJpaRepository.save(seat);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

}
