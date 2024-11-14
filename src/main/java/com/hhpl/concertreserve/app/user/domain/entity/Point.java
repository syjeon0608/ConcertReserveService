package com.hhpl.concertreserve.app.user.domain.entity;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.user.domain.PointStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.app.common.error.ErrorType.*;

@Entity
@Getter
@NoArgsConstructor
@Table(
        indexes = @Index(name = "idx_user_id", columnList = "userId")
)
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
            throw new CoreException(INVALID_CHARGE_AMOUNT);
        }

        if (status == PointStatus.CHARGE && this.amount + changeAmount > MAXIMUM_POINT_LIMIT){
            throw new CoreException(EXCEEDS_MAXIMUM_POINT);
        }

        if (status == PointStatus.USE && this.amount < changeAmount) {
            throw new CoreException(POINT_NOT_ENOUGH);
        }
    }

}
