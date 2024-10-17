package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByConcertIdAndAvailableSeatsGreaterThan(Long concertId, int soldOutLimit);

}
