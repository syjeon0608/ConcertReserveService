package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.facade.UserFacade;
import com.hhpl.concertreserve.application.model.user.PointInfo;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.ControllerMapper;
import com.hhpl.concertreserve.interfaces.dto.payment.PointAmountResponse;
import com.hhpl.concertreserve.interfaces.dto.payment.PointChargeRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/")
public class UserController {

    private final UserFacade userFacade;

    @Operation(summary = "잔액조회 API", description = "사용자가 잔액을 조회한다.")
    @GetMapping("/points")
    public ApiResponse<PointAmountResponse> getBalance(@PathVariable Long userId) {
        PointInfo pointInfo = userFacade.getUserPoint(userId);
        PointAmountResponse response = ControllerMapper.UserMapper.toResponse(pointInfo);
        return ApiResponse.OK(response);
    }

    @Operation(summary = "잔액충전 API", description = "사용자가 금액을 충전한다.")
    @PostMapping("/points")
    public ApiResponse<PointAmountResponse> chargeBalance(@PathVariable Long userId, @RequestBody PointChargeRequest request) {
        PointInfo pointInfo = userFacade.chargePoint(userId, request.amount(), request.status());
        PointAmountResponse response = ControllerMapper.UserMapper.toResponse(pointInfo);
        return ApiResponse.OK(response);
    }

}
