package com.hhpl.concertreserve.domain.concert.model;

import com.hhpl.concertreserve.domain.concert.type.SeatStatus;
import com.hhpl.concertreserve.domain.error.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hhpl.concertreserve.domain.concert.type.SeatStatus.UNAVAILABLE;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.SEAT_IS_EXPIRED;
import static com.hhpl.concertreserve.domain.error.BusinessExceptionCode.SEAT_IS_UNAVAILABLE;

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

    public void inactive() {
        this.status = UNAVAILABLE;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiredAt);
    }

    public void reactivate() {
        if (isExpired()) {
            this.status = SeatStatus.AVAILABLE;
            this.expiredAt = null;
        }
    }

    public void validate(){
        if(isExpired()) {
            throw new BusinessException(SEAT_IS_EXPIRED);
        }

        if(isUnavailable()){
            throw new BusinessException(SEAT_IS_UNAVAILABLE);
        }
    }

    private boolean isUnavailable() {
        return this.status == SeatStatus.UNAVAILABLE;
    }

}
