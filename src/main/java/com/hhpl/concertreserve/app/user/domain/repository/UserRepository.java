package com.hhpl.concertreserve.app.user.domain.repository;

import com.hhpl.concertreserve.app.user.domain.entity.Point;
import com.hhpl.concertreserve.app.user.domain.entity.User;

public interface UserRepository {
    User findUserIdByUuid(String uuid);
    Point getPoints(Long userId);
    Point updatePoint(Point point);
}
