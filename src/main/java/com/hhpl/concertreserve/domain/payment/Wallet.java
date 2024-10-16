package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.error.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.INVALID_CHARGE_AMOUNT;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.POINT_NOT_ENOUGH;

@Entity
@Getter
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Wallet(Long userId, int amount) {
        this.userId = userId;
        this.amount = amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void charge(int amountToCharge) {
        if (amountToCharge <= 0) {
            throw new BusinessException(INVALID_CHARGE_AMOUNT);
        }
        this.amount = amount + amountToCharge;
        this.updatedAt = LocalDateTime.now();
    }

    public void checkCanUse(int useAmount) {
        if (this.amount < useAmount) {
            throw new BusinessException(POINT_NOT_ENOUGH);
        }
    }

    public void useToPayment(int amountToUse) {
        this.amount = amount - amountToUse;
        this.updatedAt = LocalDateTime.now();
    }

}
