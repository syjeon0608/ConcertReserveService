package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.dto.token.WaitingTokenCreatedResponse;
import com.hhpl.concertreserve.interfaces.dto.token.WaitingTokenCreationRequest;
import com.hhpl.concertreserve.interfaces.dto.token.WaitingTokenStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiting_tokens")
public class WaitingTokenController {

    @Operation(summary = "대기열 토큰 생성 요청 API", description = "대기열 진입 후 토큰을 생성한다.")
    @PostMapping("/")
    public ApiResponse<WaitingTokenCreatedResponse> generateToken(@RequestBody WaitingTokenCreationRequest request) {
        return ApiResponse.OK(new WaitingTokenCreatedResponse(
                1L,
                request.uuid(),
                request.concertId(),
                1L,
                "INACTIVE",
                LocalDateTime.now()
        ));
    }

    @Operation(summary = " 대기열 토큰 조회 API", description = "유저는 자신의 대기열 상태를 확인한다.")
    @GetMapping("/{tokenId}")
    public ApiResponse<WaitingTokenStatusResponse> getQueueStatus(@PathVariable Long tokenId) {
        return ApiResponse.OK(new WaitingTokenStatusResponse(
                tokenId,
                "user-uuid",
                "WAITING",
                5
        ));
    }


}
