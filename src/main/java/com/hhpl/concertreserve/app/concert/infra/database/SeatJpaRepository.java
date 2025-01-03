package com.hhpl.concertreserve.app.concert.infra.database;

import com.hhpl.concertreserve.app.concert.domain.SeatStatus;
import com.hhpl.concertreserve.app.concert.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByScheduleIdAndStatus(Long scheduleId, SeatStatus status);

    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Optional<Seat> getSelectedSeat(@Param("seatId") Long seatId);


    @Query("SELECT s FROM Seat s WHERE s.status = 'UNAVAILABLE' AND s.expiredAt < :currentTime")
    List<Seat> findExpiredSeats(@Param("currentTime") LocalDateTime currentTime);

}
