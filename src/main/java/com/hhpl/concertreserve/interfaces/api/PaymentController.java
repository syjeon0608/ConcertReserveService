package com.hhpl.concertreserve.interfaces.api;

import com.hhpl.concertreserve.interfaces.dto.payment.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class PaymentController {

    @Operation(summary = "잔액조회 API", description = "사용자가 잔액을 조회한다.")
    @GetMapping("/users/{userId}/wallets")
    public ResponseEntity<WalletAmountResponse> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(new WalletAmountResponse(
                userId,
                50000L,
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "잔액충전 API", description = "사용자가 금액을 충전한다.")
    @PostMapping("/users/{userId}/wallets")
    public ResponseEntity<WalletChargeResponse> chargeBalance(@PathVariable Long userId , @RequestBody WalletChargeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new WalletChargeResponse(
                userId,
                request.amount(),
                LocalDateTime.now(),
                150000L
        ));
    }

    @Operation(summary = "결제 API", description = "예매한 콘서트 좌석을 결제한다")
    @PostMapping("/reservations/{reservationId}/payments")
    public ResponseEntity<ReservationPaymentResponse> payForReservation(@PathVariable Long reservationId,
                                                                        @RequestBody ReservationPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationPaymentResponse(
                request.tokenId(),
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
