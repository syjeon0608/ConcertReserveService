package com.hhpl.concertreserve.app.concert.domain.entity;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.concert.domain.ReservationStatus;
import com.hhpl.concertreserve.app.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.app.common.error.ErrorType.RESERVATION_ALREADY_CANCELED;
import static com.hhpl.concertreserve.app.common.error.ErrorType.RESERVATION_ALREADY_COMPLETED;
import static com.hhpl.concertreserve.app.concert.domain.ReservationStatus.*;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public Reservation(User user, Seat seat) {
        this.user = user;
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
