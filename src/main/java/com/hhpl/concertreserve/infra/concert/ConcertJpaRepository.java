package com.hhpl.concertreserve.infra.concert;

import com.hhpl.concertreserve.domain.concert.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {

    @Query("SELECT c FROM Concert c WHERE c.openDate <= :now AND c.endDate >= :now")
    List<Concert> getAvailableConcerts(@Param("now") LocalDateTime now);

}
