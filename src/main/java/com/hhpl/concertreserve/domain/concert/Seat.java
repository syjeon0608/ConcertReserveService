package com.hhpl.concertreserve.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.SeatStatus.UNAVAILABLE;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public void makeTempReservationSeat() {
        this.status = UNAVAILABLE;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

}
