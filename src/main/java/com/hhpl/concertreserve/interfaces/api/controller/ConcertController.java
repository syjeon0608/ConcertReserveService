package com.hhpl.concertreserve.interfaces.api.controller;

import com.hhpl.concertreserve.application.facade.ConcertFacade;
import com.hhpl.concertreserve.application.model.concert.ConcertInfo;
import com.hhpl.concertreserve.application.model.concert.ReservationInfo;
import com.hhpl.concertreserve.application.model.concert.ScheduleInfo;
import com.hhpl.concertreserve.application.model.concert.SeatInfo;
import com.hhpl.concertreserve.interfaces.api.common.ApiResponse;
import com.hhpl.concertreserve.interfaces.api.mapper.ControllerMapper;
import com.hhpl.concertreserve.interfaces.dto.concert.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
public class ConcertController {

    private final ConcertFacade concertFacade;

    @Operation(summary = "콘서트 조회 API", description = "예매 가능한 콘서트 목록을 조회한다.")
    @GetMapping("/")
    public ApiResponse<List<ConcertResponse>> getAvailableConcerts() {
        List<ConcertInfo> concerts = concertFacade.getAvailableConcerts();
        List<ConcertResponse> response = ControllerMapper.ConcertMapper.toConcertResponseList(concerts);
        return ApiResponse.OK(response);
    }

    @Operation(summary = "콘서트 일정 조회 API", description = "콘서트의 예약 가능한 날짜를 조회한다.")
    @GetMapping("/{concertId}/schedules")
    public ApiResponse<List<ScheduleResponse>> getAvailableSchedules(@PathVariable Long concertId, @RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        List<ScheduleInfo> schedules = concertFacade.getAvailableSchedules(concertId);
        List<ScheduleResponse> response = ControllerMapper.ConcertMapper.toScheduleResponseList(schedules);
        return ApiResponse.OK(response);
    }

    @Operation(summary = "예약가능 좌석 조회 API", description = "선택한 콘서트 일정의 예약 가능한 좌석을 조회한다.")
    @GetMapping("/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<List<SeatResponse>> getAvailableSeats(@PathVariable Long concertId,
                                                             @PathVariable Long scheduleId,
                                                             @RequestHeader("X-WAITING-QUEUE-ID") String uuid) {
        List<SeatInfo> seats = concertFacade.getAvailableSeats(scheduleId);
        List<SeatResponse> response = ControllerMapper.ConcertMapper.toSeatResponseList(seats);
        return ApiResponse.OK(response);
    }

    @Operation(summary = "좌석 예약 요청 API", description = "사용자가 좌석 예약을 요청한다.")
    @PostMapping("/{concertId}/schedules/{scheduleId}/reservations")
    public ApiResponse<ReservationResponse> reserveSeats(@PathVariable Long concertId, @PathVariable Long scheduleId,
                                                         @RequestHeader("X-WAITING-QUEUE-ID") String uuid,
                                                         @RequestBody ReservationRequest request) {
        ReservationInfo reservation = concertFacade.createTemporarySeatReservation(uuid, request.seatId());
        ReservationResponse response = ControllerMapper.ConcertMapper.toReservationResponse(reservation);
        return ApiResponse.OK(response);
    }

}
