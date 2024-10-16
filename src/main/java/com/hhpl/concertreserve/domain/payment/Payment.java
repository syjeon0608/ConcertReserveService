package com.hhpl.concertreserve.domain.payment;

import com.hhpl.concertreserve.domain.concert.Reservation;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne
    @Column(nullable = false)
    private Reservation reservation;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Payment(Reservation reservation, int amount) {
        this.reservation = reservation;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

}
