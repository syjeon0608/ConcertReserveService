package com.hhpl.concertreserve.interfaces.api.common;

import com.hhpl.concertreserve.domain.error.BusinessException;
import com.hhpl.concertreserve.domain.error.BusinessExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ApiErrorResponse handleBusinessException(BusinessException e) {
        BusinessExceptionCode errorResponse = (BusinessExceptionCode) e.getErrorCode();
        return ApiErrorResponse.ERROR(
                errorResponse.getStatus(),
                errorResponse.getCode(),
                errorResponse.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleAllExceptions(Exception e) {
        return ApiErrorResponse.ERROR(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "서버 오류가 발생했습니다."
        );
    }

}
