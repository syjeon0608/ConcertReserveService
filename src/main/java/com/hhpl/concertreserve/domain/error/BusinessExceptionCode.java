package com.hhpl.concertreserve.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionCode implements ErrorCode {

    WALLET_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "사용자ID에 해당하는 WALLET을 찾을 수 없습니다."),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST,"충전요금은 0원 이하일 수 없습니다."),
    EXCEEDS_MAXIMUM_POINT(HttpStatus.BAD_REQUEST,"충전 후 잔액이 10,000,000원이 넘습니다."),
    UUID_MISSING(HttpStatus.BAD_REQUEST, " UUID가 누락되었습니다.."),
    UUID_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "UUID가 검증에 실패하였습니다."),
    CONCERT_ID_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "콘서트 ID가 검증에 실패하였습니다."),
    QUEUE_NOT_FOUND(HttpStatus.NOT_FOUND, "대기열에 등록되지 않았습니다."),
    QUEUE_IS_INACTIVE(HttpStatus.BAD_REQUEST,"대기열이 활성화되지 않았습니다."),
    QUEUE_IS_EXPIRED(HttpStatus.BAD_REQUEST,"대기열이 만료되었습니다."),
    POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST,"충전금액이 부족합니다."),
    SEAT_IS_EXPIRED(HttpStatus.BAD_REQUEST,"임시좌석이 만료되었습니다."),
    SEAT_IS_UNAVAILABLE(HttpStatus.BAD_REQUEST,"예약 불가능한 좌석입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND,"확인된 예약이 없습니다."),
    RESERVATION_ALREADY_CANCELED(HttpStatus.BAD_REQUEST,"이미 취소 된 예약입니다."),
    RESERVATION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST,"이미 확정 된 예약입니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND,"확인된 좌석이 없습니다."),
    SEAT_ALREADY_UNAVAILABLE(HttpStatus.BAD_REQUEST,"이미 선점된 좌석입니다."),
    SEAT_ALREADY_AVAILABLE(HttpStatus.BAD_REQUEST,"이미 활성화된 좌석입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
