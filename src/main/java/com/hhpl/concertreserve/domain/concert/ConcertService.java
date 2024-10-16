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

    public Seat makeSeatAvailableToUnavailable(Long seatId) {
        Seat selectedSeat = concertRepository.getAvailableSelectedSeat(seatId);
        selectedSeat.makeTempReservationSeat();
        return concertRepository.updateSeatStatus(selectedSeat);
    }

    public void makeSeatUnAvailableToAvailable(Long seatId) {
        Seat selectedSeat = concertRepository.getAvailableSelectedSeat(seatId);
        selectedSeat.makeAvailableSeatByExpired();
        concertRepository.updateSeatStatus(selectedSeat);
    }


    public Reservation createReservation(String uuid, Seat seat) {
        Reservation reservation = new Reservation(uuid, seat);
        return concertRepository.saveReservation(reservation);
    }

    public void makeReservationWithTemporarySeat(Long seatId){
        Seat seat = concertRepository.getAvailableSelectedSeat(seatId);
        seat.makeTempReservationSeat();
        concertRepository.updateSeatStatus(seat);
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
        reservation.makeStatusComplete();
        concertRepository.saveReservation(reservation);
    }
}
