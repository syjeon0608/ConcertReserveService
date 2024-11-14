package com.hhpl.concertreserve.app.payment.api.controller;

import com.hhpl.concertreserve.app.common.api.ApiResponse;
import com.hhpl.concertreserve.app.common.mapper.ControllerMapper;
import com.hhpl.concertreserve.app.payment.api.dto.ReservationPaymentRequest;
import com.hhpl.concertreserve.app.payment.api.dto.ReservationPaymentResponse;
import com.hhpl.concertreserve.app.payment.application.PaymentFacade;
import com.hhpl.concertreserve.app.payment.domain.PaymentInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class PaymentController {
    private final PaymentFacade paymentFacade;

    @Operation(summary = "결제 API", description = "예매한 콘서트 좌석을 결제한다")
    @PostMapping("/reservations/{reservationId}/payments")
    public ApiResponse<ReservationPaymentResponse> payForReservation(@PathVariable Long reservationId,
                                                                     @RequestBody ReservationPaymentRequest request,
                                                                     @RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        PaymentInfo paymentInfo = paymentFacade.processPayment(reservationId, request.userId(), uuid);
        ReservationPaymentResponse response = ControllerMapper.PaymentMapper.toResponse(paymentInfo);
        return ApiResponse.OK(response);
    }
}
