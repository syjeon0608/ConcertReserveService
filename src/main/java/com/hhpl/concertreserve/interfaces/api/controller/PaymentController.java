package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.facade.PaymentFacade;
import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.PaymentMapper;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentRequest;
import com.hhpl.concertreserve.interfaces.dto.payment.ReservationPaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class PaymentController {
    private final WaitingQueueFacade waitingQueueFacade;
    private final PaymentFacade paymentFacade;
    private final PaymentMapper paymentMapper;

    @Operation(summary = "결제 API", description = "예매한 콘서트 좌석을 결제한다")
    @PostMapping("/reservations/{reservationId}/payments")
    public ApiResponse<ReservationPaymentResponse> payForReservation(@PathVariable Long reservationId,
                                                                     @RequestBody ReservationPaymentRequest request,
                                                                     @RequestHeader("X-QUEUE-ID") String uuid) {
        waitingQueueFacade.validateQueueStatusForUser(uuid, request.concertId());
        Payment payment = paymentFacade.processPayment(reservationId, request.userId(), request.amount());
        ReservationPaymentResponse response = paymentMapper.toPaymentResponse(payment);
        return ApiResponse.OK(response);
    }
}
