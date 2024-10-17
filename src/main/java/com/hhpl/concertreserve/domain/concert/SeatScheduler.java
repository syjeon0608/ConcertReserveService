package com.hhpl.concertreserve.domain.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatScheduler {
    private final ConcertService concertService;

    @Scheduled(fixedRate = 60000)
    public void processExpiredSeats() {
        concertService.makeExpiredSeatsAvailable();
    }
}
