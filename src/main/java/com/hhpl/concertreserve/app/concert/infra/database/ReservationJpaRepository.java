package com.hhpl.concertreserve.app.concert.infra.database;

import com.hhpl.concertreserve.app.concert.domain.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.seat.id = :seatId")
    Reservation findBySeatId(@Param("seatId") Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Reservation> findById(Long reservationId);
}
