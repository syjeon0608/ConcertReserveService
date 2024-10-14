package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueCreatedResponse;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueCreateRequest;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiting-queues")
public class WaitingQueueController {

    @Operation(summary = "대기열 생성 요청 API", description = "대기열을 생성한다.")
    @PostMapping("/")
    public ApiResponse<WaitingQueueCreatedResponse> generateToken(@RequestBody WaitingQueueCreateRequest request) {
        return ApiResponse.OK(new WaitingQueueCreatedResponse(
                1L,
                request.uuid(),
                request.concertId(),
                1L,
                "INACTIVE",
                LocalDateTime.now()
        ));
    }

    @Operation(summary = " 대기열 조회 API", description = "유저는 자신의 대기열 상태를 확인한다.")
    @GetMapping("/{queueId}")
    public ApiResponse<WaitingQueueStatusResponse> getQueueStatus(@PathVariable Long queueId) {
        return ApiResponse.OK(new WaitingQueueStatusResponse(
                queueId,
                "user-uuid",
                "WAITING",
                5
        ));
    }


}
