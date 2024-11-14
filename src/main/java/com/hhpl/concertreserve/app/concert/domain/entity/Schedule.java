package com.hhpl.concertreserve.app.concert.domain.entity;

import com.hhpl.concertreserve.app.common.error.CoreException;
import com.hhpl.concertreserve.app.common.error.ErrorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(nullable = false)
    private LocalDateTime concertDate;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false)
    private int totalSeats;

    public void decreaseAvailableSeats() {
        if (this.availableSeats > 0) {
            this.availableSeats--;
        } else {
            throw new CoreException(ErrorType.SEAT_NOT_FOUND);
        }
    }
}
