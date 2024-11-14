package com.hhpl.concertreserve.app.user.infra.database;

import com.hhpl.concertreserve.app.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByUuid(String uuid);
}
