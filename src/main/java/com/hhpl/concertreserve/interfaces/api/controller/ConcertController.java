package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.dto.concert.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
public class ConcertController {

    @Operation(summary = "콘서트 일정 조회 API", description = "콘서트의 예약 가능한 날짜를 조회한다.")
    @GetMapping("/{concertId}/schedules")

    public ApiResponse<SchedulesResponse> getSchedules(@PathVariable Long concertId,
                                                       @RequestHeader("X-TOKEN-ID") Long tokenId) {
        SchedulesResponse schedulesResponse = new SchedulesResponse(
                concertId,
                "콘서트 제목",
                "콘서트 설명",
                LocalDateTime.of(2024, 10, 1, 10, 0),
                List.of(
                        new ScheduleInfo(LocalDate.of(2024, 10, 15), 30),
                        new ScheduleInfo(LocalDate.of(2024, 10, 16), 40)
                )
        );
        return ApiResponse.OK(schedulesResponse);
    }

    @Operation(summary = "예약가능 좌석 조회 API", description = "선택한 콘서트 일정의 예약 가능한 좌석을 조회한다.")
    @GetMapping("/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<AvailableSeatsResponse> getSeats(@PathVariable Long concertId,
                                                        @PathVariable Long scheduleId,
                                                        @RequestHeader("X-TOKEN-ID") Long tokenId) {
        return ApiResponse.OK(new AvailableSeatsResponse(
                concertId,
                scheduleId,
                List.of(1, 2, 3, 4, 5)
        ));
    }

    @Operation(summary = "좌석 예약 요청 API", description = "사용자가 좌석 예약을 요청한다.")
    @PostMapping("/{concertId}/schedules/{scheduleId}/reservations")
    public ApiResponse<ReservationResponse> reserveSeats(@PathVariable Long concertId,
                                                         @PathVariable Long scheduleId,
                                                         @RequestHeader("X-TOKEN-ID") Long tokenId,
                                                         @RequestBody ReservationRequest request) {
        return ApiResponse.OK(new ReservationResponse(
                request.tokenId(),
                concertId,
                scheduleId,
                request.seatNumber(),
                "TEMPORARY",
                "TEMPORARY",
                LocalDateTime.now()
        ));
    }

}
