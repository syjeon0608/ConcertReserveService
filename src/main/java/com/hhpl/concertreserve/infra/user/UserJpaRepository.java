package com.hhpl.concertreserve.infra.user;

import com.hhpl.concertreserve.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Long findByUuid(String uuid);
}
