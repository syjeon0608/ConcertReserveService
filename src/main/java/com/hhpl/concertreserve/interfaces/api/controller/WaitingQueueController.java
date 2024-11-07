package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.application.model.waitingqueue.WaitingQueueInfo;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.ControllerMapper;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueCreateRequest;
import com.hhpl.concertreserve.interfaces.dto.waitingqueue.WaitingQueueResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/waiting-queues")
public class WaitingQueueController {

    private final WaitingQueueFacade waitingQueueFacade;

    @Operation(summary = "대기열 생성 요청 API", description = "대기열을 생성한다.")
    @PostMapping("/")
    public ApiResponse<?> generateToken(@RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        waitingQueueFacade.enterWaitingQueue(uuid);
        return ApiResponse.OK();
    }

    @Operation(summary = " 대기열 조회 API", description = "유저는 자신의 대기열 상태를 확인한다.")
    @GetMapping("/")
    public ApiResponse<WaitingQueueResponse> getQueueStatus(@RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        WaitingQueueInfo waitingQueueInfo =  waitingQueueFacade.getWaitingQueueInfoForUser(uuid);
        WaitingQueueResponse response = ControllerMapper.WaitingQueueMapper.toResponse(waitingQueueInfo);
        return ApiResponse.OK(response);
    }

}
