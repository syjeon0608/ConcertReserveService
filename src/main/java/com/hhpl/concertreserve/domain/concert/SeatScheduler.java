package com.hhpl.concertreserve.domain.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatScheduler {
    private final ConcertRepository concertRepository;
    private final ConcertService concertService;

    @Scheduled(fixedRate = 60000)
    public void processExpiredSeats() {
        List<Seat> expiredSeats = concertRepository.findExpiredSeats(LocalDateTime.now());

        expiredSeats.forEach(seat -> {
            concertService.makeSeatUnAvailableToAvailable(seat.getId());

            Reservation reservation = concertRepository.findBySeatId(seat.getId());
            reservation.makeStatusCancel();

            concertRepository.saveReservation(reservation);
            concertRepository.updateSeatStatus(seat);
        });
    }
}
