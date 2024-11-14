package com.hhpl.concertreserve.app.concert.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatScheduler {
    private final ConcertService concertService;

    @Scheduled(fixedRate = 60000)
    public void processExpiredSeats() {
        concertService.makeExpiredSeatsAvailable();
    }
}
