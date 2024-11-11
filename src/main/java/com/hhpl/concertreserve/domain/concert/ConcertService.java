package com.hhpl.concertreserve.domain.concert;

import com.hhpl.concertreserve.domain.concert.model.Concert;
import com.hhpl.concertreserve.domain.concert.model.Reservation;
import com.hhpl.concertreserve.domain.concert.model.Schedule;
import com.hhpl.concertreserve.domain.concert.model.Seat;
import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.error.ErrorType;
import com.hhpl.concertreserve.domain.user.UserRepository;
import com.hhpl.concertreserve.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertCachingRepository concertCachingRepository;
    private final UserRepository userRepository;

    public List<Concert> getAvailableConcerts() {
        LocalDateTime now = LocalDateTime.now();
        return concertCachingRepository.getAvailableConcerts(now);
    }

    public List<Schedule> getAvailableSchedules(Long concertId) {
        return concertRepository.getSchedulesWithAvailableSeatCount(concertId);
    }

    public List<Seat> getAvailableSeats(Long scheduleId) {
        return concertRepository.getAvailableSeats(scheduleId, SeatStatus.AVAILABLE);
    }

    @Transactional
    public Reservation createReservation(String uuid, Long seatId) {
        User user = userRepository.findUserIdByUuid(uuid);
        Seat selectedSeat = concertRepository.getSelectedSeat(seatId);
        Schedule schedule = selectedSeat.getSchedule();
        try {
            selectedSeat.inactive();
            schedule.decreaseAvailableSeats();
            Reservation reservation = new Reservation(user, selectedSeat);
            return concertRepository.save(reservation);
        } catch (Exception e) {
            throw new CoreException(ErrorType.SEAT_ALREADY_UNAVAILABLE);
        }
    }

    @Transactional
    public void makeExpiredSeatsAvailable() {
        List<Seat> expiredSeats = concertRepository.findExpiredSeatsToBeAvailable(LocalDateTime.now());
        expiredSeats.forEach(seat -> {
            seat.reactivate();
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

    public void validateSeatStatusForPayment(Long reservationId) {
        Seat seat = getReservationSeat(reservationId);
        seat.validateForSeatExpired();
    }

    public Reservation convertReservationToComplete(Long reservationId) {
        Reservation reservation = concertRepository.getMyReservation(reservationId);
        reservation.completeReservation();
        return concertRepository.save(reservation);
    }

}
