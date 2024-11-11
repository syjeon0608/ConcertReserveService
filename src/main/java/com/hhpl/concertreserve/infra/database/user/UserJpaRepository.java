package com.hhpl.concertreserve.infra.database.user;

import com.hhpl.concertreserve.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByUuid(String uuid);
}
