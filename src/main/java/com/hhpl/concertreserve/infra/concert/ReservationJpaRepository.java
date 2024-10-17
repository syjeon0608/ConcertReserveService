package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {

    @Query("SELECT r FROM Reservation r WHERE r.seat.id = :seatId")
    Reservation findBySeatId(@Param("seatId") Long seatId);


}
