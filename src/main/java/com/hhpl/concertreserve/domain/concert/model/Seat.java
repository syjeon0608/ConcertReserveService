package com.hhpl.concertreserve.domain.concert.model;

import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.domain.error.CoreException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.type.SeatStatus.UNAVAILABLE;
import static com.hhpl.concertreserve.domain.error.ErrorType.*;

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

    private LocalDateTime expiredAt;

    public void inactive() {
        if (this.status == SeatStatus.UNAVAILABLE) {
            throw new CoreException(SEAT_ALREADY_UNAVAILABLE);
        }
        this.status = UNAVAILABLE;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    public void reactivate() {
        if (this.status == SeatStatus.AVAILABLE) {
            throw new CoreException(SEAT_ALREADY_AVAILABLE);
        }
        if (isExpired()) {
            this.status = SeatStatus.AVAILABLE;
            this.expiredAt = null;
        }
    }

    public void validateForSeatExpired(){
        if(isExpired()) {
            throw new CoreException(SEAT_IS_EXPIRED);
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }


}
