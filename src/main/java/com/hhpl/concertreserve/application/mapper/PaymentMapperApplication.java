package com.hhpl.concertreserve.application.mapper;

import com.hhpl.concertreserve.application.model.PaymentInfo;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperApplication {

    public PaymentInfo from(Payment payment) {
        return new PaymentInfo(
                payment.getReservation().getId(),
                payment.getUserId(),
                payment.getReservation().getSeat().getId(),
                payment.getAmount(),
                payment.getReservation().getReservationStatus().name(),
                payment.getCreatedAt()
        );
    }
}
