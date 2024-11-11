package com.hhpl.concertreserve.domain.user;

import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.User;

public interface UserRepository {
    User findUserIdByUuid(String uuid);
    Point getPoints(Long userId);
    Point updatePoint(Point point);
}
