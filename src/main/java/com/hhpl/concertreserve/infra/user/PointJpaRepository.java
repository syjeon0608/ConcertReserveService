package com.hhpl.concertreserve.infra.user;

import com.hhpl.concertreserve.domain.user.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point,Long> {
    Optional<Point> findByUserId(Long userId);
}
