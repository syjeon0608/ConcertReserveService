package com.hhpl.concertreserve.infra.database.concert;

import com.hhpl.concertreserve.domain.concert.model.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.seat.id = :seatId")
    Reservation findBySeatId(@Param("seatId") Long seatId);


}
