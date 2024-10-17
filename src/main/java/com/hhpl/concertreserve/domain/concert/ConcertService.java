package com.hhpl.concertreserve.domain.concert;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void reserveSeatTemporarily(Long seatId) {
        Seat selectedSeat = concertRepository.getAvailableSelectedSeat(seatId);
        selectedSeat.makeTempReservationSeat();
        concertRepository.save(selectedSeat);
    }

    public Reservation createReservation(String uuid, Long seatId) {
        Seat seat = concertRepository.getSeatById(seatId);
        Reservation reservation = new Reservation(uuid, seat);
       return concertRepository.save(reservation);
    }

    @Transactional
    public void makeExpiredSeatsAvailable() {
        List<Seat> expiredSeats = concertRepository.findExpiredSeatsToBeAvailable(LocalDateTime.now());
        expiredSeats.forEach(seat -> {
            seat.makeAvailableAfterExpiration();
            concertRepository.save(seat);

            Reservation reservation = concertRepository.findBySeatId(seat.getId());
            reservation.cancelReservation();
            concertRepository.save(reservation);
        });
    }

    public Reservation getReservationInfo(Long reservationId){
        return concertRepository.getMyReservation(reservationId);
    }

    public Seat getReservationSeat(Long reservationId) {
        Reservation reservation = getReservationInfo(reservationId);
        return reservation.getSeat();
    }

    public void validateSeatStatus(Long reservationId) {
        Seat seat = getReservationSeat(reservationId);
        seat.checkIfExpired();
    }

    public void completeReservationByPayment(Long reservationId){
        Reservation reservation = getReservationInfo(reservationId);
        reservation.completeReservation();
        concertRepository.save(reservation);
    }


}
