package com.hhpl.concertreserve.app.user.domain.service;

import com.hhpl.concertreserve.app.user.domain.PointStatus;
import com.hhpl.concertreserve.app.user.domain.entity.Point;
import com.hhpl.concertreserve.app.user.domain.repository.UserRepository;
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
