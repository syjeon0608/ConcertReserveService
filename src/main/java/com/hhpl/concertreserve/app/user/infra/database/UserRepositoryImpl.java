package com.hhpl.concertreserve.app.user.infra.database;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.user.domain.entity.Point;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import com.hhpl.concertreserve.app.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.hhpl.concertreserve.app.common.error.ErrorType.USER_NOT_FOUND;
import static com.hhpl.concertreserve.app.common.error.ErrorType.WALLET_NOT_FOUND_ERROR;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final PointJpaRepository pointJpaRepository;

    @Override
    public User findUserIdByUuid(String uuid) {
        return userJpaRepository.findByUuid(uuid)
                .orElseThrow(()->new CoreException(USER_NOT_FOUND));
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
