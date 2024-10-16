package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.Seat;
import com.hhpl.concertreserve.domain.concert.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByScheduleIdAndStatus(Long scheduleId, SeatStatus status);
}
