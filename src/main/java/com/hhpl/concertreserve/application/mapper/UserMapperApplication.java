package com.hhpl.concertreserve.application.mapper;

import com.hhpl.concertreserve.application.model.PointInfo;
import com.hhpl.concertreserve.domain.user.model.Point;
import org.springframework.stereotype.Component;

@Component
public class UserMapperApplication {

    public PointInfo from(Point point) {
        return new PointInfo(
                point.getUserId(),
                point.getAmount(),
                point.getUpdatedAt()
        );
    }
}
