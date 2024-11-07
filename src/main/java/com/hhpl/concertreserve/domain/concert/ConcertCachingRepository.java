package com.hhpl.concertreserve.domain.concert;

import com.hhpl.concertreserve.domain.concert.model.Concert;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertCachingRepository {
    List<Concert> getAvailableConcerts(LocalDateTime now);
}
