package com.hhpl.concertreserve.domain.concert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.ReservationStatus.*;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int totalAmount;

    public Reservation(String uuid, Seat seat) {
        this.uuid = uuid;
        this.seat = seat;
        this.reservationStatus = TEMPORARY;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = seat.getPrice();
    }

    public void cancelReservation() {
        this.reservationStatus = CANCEL;
        this.updatedAt = LocalDateTime.now();
    }

    public void completeReservation(){
        this.reservationStatus = COMPLETE;
        this.updatedAt = LocalDateTime.now();
    }
}
