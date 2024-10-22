package com.hhpl.concertreserve.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    WALLET_NOT_FOUND_ERROR(ErrorCode.NOT_FOUND, "사용자ID에 해당하는 WALLET을 찾을 수 없습니다.", LogLevel.INFO),
    INVALID_CHARGE_AMOUNT(ErrorCode.BUSINESS_EXCEPTION,"충전요금은 0원 이하일 수 없습니다.", LogLevel.INFO),
    EXCEEDS_MAXIMUM_POINT(ErrorCode.BUSINESS_EXCEPTION,"충전 후 잔액이 10,000,000원이 넘습니다.", LogLevel.INFO),
    UUID_MISSING(ErrorCode.BAD_REQUEST, " UUID가 누락되었습니다.", LogLevel.INFO),
    UUID_VALIDATION_FAILED(ErrorCode.VALIDATION_EXCEPTION, "UUID가 검증에 실패하였습니다.", LogLevel.INFO),
    CONCERT_ID_VALIDATION_FAILED(ErrorCode.VALIDATION_EXCEPTION, "콘서트 ID가 검증에 실패하였습니다.", LogLevel.INFO),
    QUEUE_NOT_FOUND(ErrorCode.NOT_FOUND, "대기열에 등록되지 않았습니다.", LogLevel.INFO),
    QUEUE_IS_INACTIVE(ErrorCode.AUTHORIZATION_ERROR,"대기열이 활성화되지 않았습니다.", LogLevel.INFO),
    QUEUE_IS_EXPIRED(ErrorCode.AUTHORIZATION_ERROR,"대기열이 만료되었습니다.", LogLevel.INFO),
    POINT_NOT_ENOUGH(ErrorCode.BUSINESS_EXCEPTION,"충전금액이 부족합니다.", LogLevel.INFO),
    SEAT_IS_EXPIRED(ErrorCode.BUSINESS_EXCEPTION,"임시좌석이 만료되었습니다.", LogLevel.INFO),
    SEAT_IS_UNAVAILABLE(ErrorCode.BUSINESS_EXCEPTION,"예약 불가능한 좌석입니다.", LogLevel.INFO),
    RESERVATION_NOT_FOUND(ErrorCode.NOT_FOUND,"확인된 예약이 없습니다.", LogLevel.INFO),
    RESERVATION_ALREADY_CANCELED(ErrorCode.BUSINESS_EXCEPTION,"이미 취소 된 예약입니다.", LogLevel.INFO),
    RESERVATION_ALREADY_COMPLETED(ErrorCode.BUSINESS_EXCEPTION,"이미 확정 된 예약입니다.", LogLevel.INFO),
    SEAT_NOT_FOUND(ErrorCode.NOT_FOUND,"확인된 좌석이 없습니다.", LogLevel.INFO),
    SEAT_ALREADY_UNAVAILABLE(ErrorCode.BUSINESS_EXCEPTION,"이미 선점된 좌석입니다.", LogLevel.INFO),
    SEAT_ALREADY_AVAILABLE(ErrorCode.BUSINESS_EXCEPTION,"이미 활성화된 좌석입니다.", LogLevel.INFO);

    private final ErrorCode errorCode;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(ErrorCode errorCode, String message, LogLevel logLevel) {
        this.errorCode = errorCode;
        this.message = message;
        this.logLevel = logLevel;
    }

}
