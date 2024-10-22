package com.hhpl.concertreserve.infra.user;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.user.UserRepository;
import com.hhpl.concertreserve.domain.user.model.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.hhpl.concertreserve.domain.error.ErrorType.WALLET_NOT_FOUND_ERROR;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final PointJpaRepository pointJpaRepository;

    @Override
    public Long findUserIdByUuid(String uuid) {
        return userJpaRepository.findByUuid(uuid);
    }

    @Override
    public Point getPoints(Long userId) {
        return pointJpaRepository.findByUserId(userId)
                .orElseThrow(()-> new CoreException(WALLET_NOT_FOUND_ERROR));
    }

    @Override
    public Point updatePoint(Point point) {
        return pointJpaRepository.save(point);
    }
}
