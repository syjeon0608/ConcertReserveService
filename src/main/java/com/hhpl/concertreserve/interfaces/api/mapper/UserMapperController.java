package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.application.model.PointInfo;
import com.hhpl.concertreserve.interfaces.dto.payment.PointAmountResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapperController {

    public PointAmountResponse toResponse(PointInfo point) {
        return new PointAmountResponse(
                point.userId(),
                point.amount(),
                point.updatedAt()
        );
    }

}
