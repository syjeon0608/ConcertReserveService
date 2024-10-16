package com.hhpl.concertreserve.domain.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public List<Concert> getAvailableConcerts() {
        LocalDateTime now = LocalDateTime.now();
        return concertRepository.getAvailableConcerts(now);
    }

    public List<Schedule> getAvailableSchedules(Long concertId) {
        int soldOutLimit = 0;
        return concertRepository.getSchedulesWithAvailableSeats(concertId, soldOutLimit);
    }

    public List<Seat> getAvailableSeats(Long scheduleId) {
        return concertRepository.getAvailableSeats(scheduleId, SeatStatus.AVAILABLE);
    }

}