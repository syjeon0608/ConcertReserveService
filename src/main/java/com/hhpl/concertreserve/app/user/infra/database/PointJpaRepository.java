package com.hhpl.concertreserve.app.user.infra.database;

import com.hhpl.concertreserve.app.user.domain.entity.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Point> findByUserId(Long userId);
}
