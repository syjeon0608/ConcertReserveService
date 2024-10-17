package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.concert.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Payment(Long userId, Reservation reservation, int amount) {
        this.userId = userId;
        this.reservation = reservation;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

}
