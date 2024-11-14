package com.hhpl.concertreserve.app.concert.domain.repository;

import com.hhpl.concertreserve.app.concert.domain.entity.Concert;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertCachingRepository {
    List<Concert> getAvailableConcerts(LocalDateTime now);
}
