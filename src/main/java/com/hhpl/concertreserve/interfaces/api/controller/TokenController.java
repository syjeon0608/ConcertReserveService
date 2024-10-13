package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.dto.token.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tokens")
public class TokenController {

    @Operation(summary = "대기열 토큰 생성 요청 API", description = "대기열 진입 후 토큰을 생성한다.")
    @PostMapping("/")
    public ApiResponse<TokenResponse> generateToken(@RequestBody TokenCreationRequest request) {
        return ApiResponse.OK(new TokenResponse(
                1L,
                request.uuid(),
                request.concertId(),
                "INACTIVE",
                LocalDateTime.now()
        ));
    }

    @Operation(summary = " 대기열 토큰 조회 API", description = "유저는 자신의 대기열 상태를 확인한다.")
    @GetMapping("/{tokenId}")
    public ApiResponse<UserQueueStatusResponse> getQueueStatus(@PathVariable Long tokenId) {
        return ApiResponse.OK(new UserQueueStatusResponse(
                1L,
                "user-uuid",
                "WAITING",
                5
        ));
    }

    @Operation(summary = "토큰 활성화 API", description = "대기가 완료되면 비활성 토큰을 활성토큰으로 전환한다.")
    @PatchMapping("/{tokenId}")
    public ApiResponse<TokenActivationResponse> activateUsers(@PathVariable Long tokenId) {
        return ApiResponse.OK(new TokenActivationResponse(
                1L,
                "user-uuid",
                "ACTIVE",
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "토큰 검증 API", description = "활성된 토큰인지 검사한다.")
    @RequestMapping(value = "/{tokenId}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> validateToken(@PathVariable Long tokenId) {
        return ResponseEntity.ok().build();
    }

}
