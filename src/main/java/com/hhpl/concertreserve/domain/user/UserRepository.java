package com.hhpl.concertreserve.domain.user;

import com.hhpl.concertreserve.domain.user.model.Point;

public interface UserRepository {
    Long findUserIdByUuid(String uuid);
    Point getPoints(Long userId);
    Point updatePoint(Point point);
}
