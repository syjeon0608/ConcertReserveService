package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.dto.payment.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class PaymentController {

    @Operation(summary = "잔액조회 API", description = "사용자가 잔액을 조회한다.")
    @GetMapping("/users/{userId}/wallets")
    public ApiResponse<WalletAmountResponse> getBalance(@PathVariable Long userId) {
        return ApiResponse.OK(new WalletAmountResponse(
                userId,
                50000L,
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "잔액충전 API", description = "사용자가 금액을 충전한다.")
    @PostMapping("/users/{userId}/wallets")
    public ApiResponse<WalletChargeResponse> chargeBalance(@PathVariable Long userId, @RequestBody WalletChargeRequest request) {
        return ApiResponse.OK(new WalletChargeResponse(
                userId,
                request.amount(),
                LocalDateTime.now(),
                150000L
        ));
    }

    @Operation(summary = "결제 API", description = "예매한 콘서트 좌석을 결제한다")
    @PostMapping("/reservations/{reservationId}/payments")


    public ApiResponse<ReservationPaymentResponse> payForReservation(@PathVariable Long reservationId,
                                                                     @RequestBody ReservationPaymentRequest request,
                                                                     @RequestHeader("X-QUEUE-ID") Long queueId) {
        return ApiResponse.OK(new ReservationPaymentResponse(

                queueId,
                reservationId,
                1L,
                2L,
                13,
                "CONFIRMED",
                "CONFIRMED",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                request.amount()
        ));
    }
}
