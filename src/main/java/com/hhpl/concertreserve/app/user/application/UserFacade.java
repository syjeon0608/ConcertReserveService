package com.hhpl.concertreserve.app.user.application;

import com.hhpl.concertreserve.app.common.mapper.ApplicationMapper;
import com.hhpl.concertreserve.app.user.domain.PointInfo;
import com.hhpl.concertreserve.app.user.domain.PointStatus;
import com.hhpl.concertreserve.app.user.domain.entity.Point;
import com.hhpl.concertreserve.app.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public PointInfo getUserPoint(Long userId) {
        Point point=  userService.getUserPoint(userId);
        return ApplicationMapper.UserMapper.from(point);
    }
    
    public PointInfo chargePoint(Long userId, int amountToCharge, PointStatus status) {
        Point point =  userService.updateUserPoint(userId, amountToCharge, status);
        return ApplicationMapper.UserMapper.from(point);
    }

}
