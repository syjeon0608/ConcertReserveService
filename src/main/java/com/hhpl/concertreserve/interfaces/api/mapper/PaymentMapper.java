package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public ReservationPaymentResponse toPaymentResponse(Payment payment) {
        return new ReservationPaymentResponse(
                payment.getReservation().getId(),
                payment.getUserId(),
                payment.getReservation().getSeat().getId(),
                payment.getAmount(),
                payment.getReservation().getReservationStatus().name(),
                payment.getCreatedAt()
        );
    }

}
