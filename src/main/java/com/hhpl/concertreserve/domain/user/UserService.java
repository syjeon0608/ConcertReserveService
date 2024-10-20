package com.hhpl.concertreserve.domain.user;

import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Point getUserPoint(Long userId) {
        return userRepository.getPoints(userId);
    }

    public Point updateUserPoint(Long userId, int amountToCharge, PointStatus status) {
        Point point = userRepository.getPoints(userId);
        point.validate(amountToCharge, status);
        point.adjust(amountToCharge,status);
        return userRepository.updatePoint(point);
    }

}
