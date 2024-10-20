package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.facade.WaitingQueueFacade;
import com.hhpl.concertreserve.application.model.WaitingQueueInfo;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.WaitingQueueMapperController;
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
    private final WaitingQueueMapperController waitingQueueMapper;

    @Operation(summary = "대기열 생성 요청 API", description = "대기열을 생성한다.")
    @PostMapping("/")
    public ApiResponse<WaitingQueueResponse> generateToken(@RequestBody WaitingQueueCreateRequest request) {
        WaitingQueueInfo waitingQueueInfo = waitingQueueFacade.enterWaitingQueueForConcert(request.uuid(), request.concertId());
        WaitingQueueResponse response = waitingQueueMapper.toResponse(waitingQueueInfo);
        return ApiResponse.OK(response);
    }

    @Operation(summary = " 대기열 조회 API", description = "유저는 자신의 대기열 상태를 확인한다.")
    @GetMapping("/concerts/{concertId}")
    public ApiResponse<WaitingQueueResponse> getQueueStatus(@RequestHeader("X-User-UUID") String uuid, @PathVariable Long concertId) {
        WaitingQueueInfo waitingQueueInfo =  waitingQueueFacade.getWaitingQueueInfoForUser(uuid, concertId);
        WaitingQueueResponse response = waitingQueueMapper.toResponse(waitingQueueInfo);
        return ApiResponse.OK(response);
    }

}
