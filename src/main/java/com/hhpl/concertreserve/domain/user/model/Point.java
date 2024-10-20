package com.hhpl.concertreserve.domain.user.model;

import com.hhpl.concertreserve.domain.error.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.*;

@Entity
@Getter
@NoArgsConstructor
public class Point {

    private static final int MAXIMUM_POINT_LIMIT = 1000000;
    private static final int MINIMUM_CHARGE_AMOUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Point(Long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void adjust(int changeAmount, PointStatus status) {
        if (status == PointStatus.CHARGE) {
            this.amount = amount + changeAmount;
        } else if (status == PointStatus.USE) {
            this.amount = amount - changeAmount;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void validate(int changeAmount, PointStatus status) {
        if (status == PointStatus.CHARGE && changeAmount < MINIMUM_CHARGE_AMOUNT) {
            throw new BusinessException(INVALID_CHARGE_AMOUNT);
        }

        if (status == PointStatus.CHARGE && this.amount + changeAmount > MAXIMUM_POINT_LIMIT){
            throw new BusinessException(EXCEEDS_MAXIMUM_POINT);
        }

        if (status == PointStatus.USE && this.amount < changeAmount) {
            throw new BusinessException(POINT_NOT_ENOUGH);
        }
    }

}