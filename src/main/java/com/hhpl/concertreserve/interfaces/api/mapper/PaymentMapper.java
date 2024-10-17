package com.hhpl.concertreserve.interfaces.api.mapper;

import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.payment.model.Point;
import com.hhpl.concertreserve.interfaces.dto.payment.PointAmountResponse;
import com.hhpl.concertreserve.interfaces.dto.payment.PointChargeResponse;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PointAmountResponse toPointAmountResponse(Point point) {
        return new PointAmountResponse(
                point.getUserId(),
                point.getAmount(),
                point.getUpdatedAt()
        );
    }

    public PointChargeResponse toPointChargeResponse(Point point) {
        return new PointChargeResponse(
                point.getAmount()
        );
    }

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
