package com.hhpl.concertreserve.application.facade;

import com.hhpl.concertreserve.application.mapper.ApplicationMapper;
import com.hhpl.concertreserve.application.model.user.PointInfo;
import com.hhpl.concertreserve.domain.user.UserService;
import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
