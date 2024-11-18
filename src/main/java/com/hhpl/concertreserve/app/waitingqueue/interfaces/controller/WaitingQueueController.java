package com.hhpl.concertreserve.app.waitingqueue.interfaces.controller;

import com.hhpl.concertreserve.app.common.api.ApiResponse;
import com.hhpl.concertreserve.app.common.mapper.ControllerMapper;
import com.hhpl.concertreserve.app.waitingqueue.interfaces.dto.WaitingQueueResponse;
import com.hhpl.concertreserve.app.waitingqueue.application.WaitingQueueFacade;
import com.hhpl.concertreserve.app.waitingqueue.domain.WaitingQueue;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiting-queues")
public class WaitingQueueController {

    private final WaitingQueueFacade waitingQueueFacade;

    @Operation(summary = "대기열 생성 요청 API", description = "대기열을 생성한다.")
    @PostMapping()
    public ApiResponse<?> generateToken(@RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        waitingQueueFacade.enterWaitingQueue(uuid);
        return ApiResponse.OK();
    }

    @Operation(summary = " 대기열 조회 API", description = "유저는 자신의 대기열 상태를 확인한다.")
    @GetMapping()
    public ApiResponse<WaitingQueueResponse> getQueueStatus(@RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        WaitingQueue waitingQueueInfo =  waitingQueueFacade.getWaitingQueueInfoForUser(uuid);
        WaitingQueueResponse response = ControllerMapper.WaitingQueueMapper.toResponse(waitingQueueInfo);
        return ApiResponse.OK(response);
    }

}
