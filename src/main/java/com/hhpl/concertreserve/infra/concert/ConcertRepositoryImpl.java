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
}
