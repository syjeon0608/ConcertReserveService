package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {
}
