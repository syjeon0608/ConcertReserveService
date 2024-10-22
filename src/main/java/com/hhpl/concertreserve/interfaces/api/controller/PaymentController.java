package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.facade.PaymentFacade;
import com.hhpl.concertreserve.application.model.payment.PaymentInfo;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.ControllerMapper;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentRequest;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentResponse;
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
                                                                     @RequestHeader("X-QUEUE-ID") String uuid) {
        PaymentInfo paymentInfo = paymentFacade.processPayment(reservationId, request.userId(), uuid);
        ReservationPaymentResponse response = ControllerMapper.PaymentMapper.toResponse(paymentInfo);
        return ApiResponse.OK(response);
    }
}
