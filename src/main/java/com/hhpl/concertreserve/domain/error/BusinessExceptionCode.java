package com.hhpl.concertreserve.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionCode implements ErrorCode {

    WALLET_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "사용자ID에 해당하는 WALLET을 찾을 수 없습니다."),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST,"충전요금은 0원 이하일 수 없습니다."),
    UUID_MISSING(HttpStatus.BAD_REQUEST, " UUID가 누락되었습니다.."),
    UUID_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "UUID가 검증에 실패하였습니다."),
    CONCERT_ID_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "콘서트 ID가 검증에 실패하였습니다."),
    QUEUE_NOT_FOUND(HttpStatus.NOT_FOUND, "대기열에 등록되지 않았습니다."),
    QUEUE_IS_INACTIVE(HttpStatus.BAD_REQUEST,"대기열이 활성화되지 않았습니다."),
    QUEUE_IS_EXPIRED(HttpStatus.BAD_REQUEST,"대기열이 만료되었습니다."),
    POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST,"충전금액이 부족합니다."),
    SEAT_IS_EXPIRED(HttpStatus.BAD_REQUEST,"임시좌석이 만료되었습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND,"확인된 예약이 없습니다.");

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
