package com.hhpl.concertreserve.domain.user;

import com.hhpl.concertreserve.domain.user.model.Point;
import com.hhpl.concertreserve.domain.user.model.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Point getUserPoint(Long userId) {
        return userRepository.getPoints(userId);
    }

    @Transactional
    public Point updateUserPoint(Long userId, int amount, PointStatus status) {
        Point point = userRepository.getPoints(userId);
        point.validate(amount, status);
        point.adjust(amount,status);
        return userRepository.updatePoint(point);
    }

}
