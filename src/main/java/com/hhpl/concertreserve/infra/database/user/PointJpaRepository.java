package com.hhpl.concertreserve.infra.database.user;

import com.hhpl.concertreserve.domain.user.model.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Point> findByUserId(Long userId);
}
