package com.hhpl.concertreserve.infra.user;

import com.hhpl.concertreserve.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Long findUserIdByUuid(String uuid) {
        return userJpaRepository.findByUuid(uuid);
    }
}
