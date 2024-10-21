package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.application.model.PaymentInfo;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperController {

    public ReservationPaymentResponse toResponse(PaymentInfo paymentInfo) {
        return new ReservationPaymentResponse(
                paymentInfo.reservationId(),
                paymentInfo.userId(),
                paymentInfo.seatId(),
                paymentInfo.amount(),
                paymentInfo.reservationStatus(),
                paymentInfo.paymentDate()
        );
    }

}
