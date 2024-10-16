package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.Seat;
import com.hhpl.concertreserve.domain.concert.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByScheduleIdAndStatus(Long scheduleId, SeatStatus status);

    @Query("SELECT s FROM Seat s WHERE s.id = :seatId AND s.status = 'AVAILABLE'")
    Seat getAvailableSelectedSeat(@Param("seatId") Long seatId);


    @Query("SELECT s FROM Seat s WHERE s.status = 'UNAVAILABLE' AND s.expiredAt < :currentTime")
    List<Seat> findExpiredSeats(@Param("currentTime") LocalDateTime currentTime);
}
