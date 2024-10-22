package com.hhpl.concertreserve.domain.concert.model;

import com.hhpl.concertreserve.domain.concert.type.ReservationStatus;
import com.hhpl.concertreserve.domain.error.CoreException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.type.ReservationStatus.*;
import static com.hhpl.concertreserve.domain.error.ErrorType.RESERVATION_ALREADY_CANCELED;
import static com.hhpl.concertreserve.domain.error.ErrorType.RESERVATION_ALREADY_COMPLETED;

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
        if (this.reservationStatus == CANCEL) {
            throw new CoreException(RESERVATION_ALREADY_CANCELED);
        }
        this.reservationStatus = CANCEL;
        this.updatedAt = LocalDateTime.now();
    }

    public void completeReservation(){
        if (this.reservationStatus == COMPLETE) {
            throw new CoreException(RESERVATION_ALREADY_COMPLETED);
        }
        this.reservationStatus = COMPLETE;
        this.updatedAt = LocalDateTime.now();
    }
}
