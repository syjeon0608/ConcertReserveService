package com.hhpl.concertreserve.app.concert.infra.database;

import com.hhpl.concertreserve.app.concert.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findByConcertId(Long concertId);

}
