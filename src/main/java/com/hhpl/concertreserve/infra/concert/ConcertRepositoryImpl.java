package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.*;
import com.hhpl.concertreserve.domain.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.RESERVATION_NOT_FOUND;

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

    @Override
    public Reservation getMyReservation(Long reservationId) {
        return reservationJpaRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(RESERVATION_NOT_FOUND));
    }

    @Override
    public List<Seat> findExpiredSeats(LocalDateTime now) {
        return seatJpaRepository.findExpiredSeats(now);
    }

    @Override
    public Reservation findBySeatId(Long seatId) {
        return reservationJpaRepository.findBySeatId(seatId);
    }


}
