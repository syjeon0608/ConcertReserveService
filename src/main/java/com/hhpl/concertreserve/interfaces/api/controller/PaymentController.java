package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.PaymentFacade;
import com.hhpl.concertreserve.application.WaitingQueueFacade;
import com.hhpl.concertreserve.domain.payment.model.Payment;
import com.hhpl.concertreserve.domain.payment.model.Point;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.PaymentMapper;
import com.hhpl.concertreserve.interfaces.dto.payment.*;
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

    @Operation(summary = "잔액조회 API", description = "사용자가 잔액을 조회한다.")
    @GetMapping("/users/{userId}/points")
    public ApiResponse<PointAmountResponse> getBalance(@PathVariable Long userId) {
        Point point = paymentFacade.getUserWallet(userId);
        PointAmountResponse response = paymentMapper.toPointAmountResponse(point);
        return ApiResponse.OK(response);
    }

    @Operation(summary = "잔액충전 API", description = "사용자가 금액을 충전한다.")
    @PostMapping("/users/{userId}/points")
    public ApiResponse<PointChargeResponse> chargeBalance(@PathVariable Long userId, @RequestBody PointChargeResponse request) {
        Point point = paymentFacade.chargePoint(userId, request.amount());
        PointChargeResponse response = paymentMapper.toPointChargeResponse(point);
        return ApiResponse.OK(response);
    }

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
