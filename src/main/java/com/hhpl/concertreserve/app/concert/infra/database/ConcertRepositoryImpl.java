package com.hhpl.concertreserve.app.concert.infra.database;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Concert;
import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import com.hhpl.concertreserve.app.concert.domain.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.hhpl.concertreserve.app.common.error.ErrorType.RESERVATION_NOT_FOUND;
import static com.hhpl.concertreserve.app.common.error.ErrorType.SEAT_NOT_FOUND;

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
    public List<Schedule> getSchedulesWithAvailableSeatCount(Long concertId) {
        return scheduleJpaRepository.findByConcertId(concertId);
    }

    @Override
    public List<Seat> getAvailableSeats(Long scheduleId, SeatStatus seatStatus) {
        return seatJpaRepository.findByScheduleIdAndStatus(scheduleId,seatStatus);
    }

    @Override
    public Seat getSelectedSeat(Long seatId) {
        return seatJpaRepository.getSelectedSeat(seatId)
                .orElseThrow(() -> new CoreException(SEAT_NOT_FOUND));
    }

    @Override
    public void save(Seat seat) {
        seatJpaRepository.save(seat);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public Reservation getMyReservation(Long reservationId) {
        return reservationJpaRepository.findById(reservationId)
                .orElseThrow(() -> new CoreException(RESERVATION_NOT_FOUND));
    }

    @Override
    public List<Seat> findExpiredSeatsToBeAvailable(LocalDateTime now) {
        return seatJpaRepository.findExpiredSeats(now);
    }

    @Override
    public Reservation findBySeatId(Long seatId) {
        return reservationJpaRepository.findBySeatId(seatId);
    }


}
