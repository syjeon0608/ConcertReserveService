package com.hhpl.concertreserve.app.payment.domain;

import com.hhpl.concertreserve.app.concert.domain.ReservationInfo;
import org.springframework.context.ApplicationEvent;

public class PaymentSuccessEvent extends ApplicationEvent {

    private final ReservationInfo reservationInfo;
    private final String token;

    public PaymentSuccessEvent(Object source, ReservationInfo reservationInfo, String token) {
        super(source);
        this.reservationInfo = reservationInfo;
        this.token = token;
    }
    public ReservationInfo getReservationInfo() {
        return reservationInfo;
    }

    public String getToken() {
        return token;
    }
}
