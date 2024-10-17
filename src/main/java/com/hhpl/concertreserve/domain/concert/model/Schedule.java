package com.hhpl.concertreserve.domain.concert.model;

import com.hhpl.concertreserve.domain.concert.model.Concert;
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
}
