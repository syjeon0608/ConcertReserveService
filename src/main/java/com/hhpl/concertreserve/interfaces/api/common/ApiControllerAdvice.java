package com.hhpl.concertreserve.interfaces.api.common;

import com.hhpl.concertreserve.domain.error.CoreException;
import com.hhpl.concertreserve.domain.error.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(CoreException.class)
    public ApiErrorResponse handleBusinessException(CoreException e) {
        ErrorType errorResponse = e.getErrorType();
        HttpStatus status;

        String payloadString = e.getPayload() != null && e.getPayload().length > 0 ? Arrays.toString(e.getPayload()) : "No payload provided";

        switch (errorResponse.getLogLevel()) {
            case ERROR -> log.error(errorResponse.getMessage() + " - Payload: " + payloadString, e);
            case WARN -> log.warn(errorResponse.getMessage() + " - Payload: " + payloadString, e);
            default -> log.info(errorResponse.getMessage() + " - Payload: " + payloadString, e);
        }

        switch (errorResponse.getErrorCode()) {
            case VALIDATION_EXCEPTION, BAD_REQUEST, BUSINESS_EXCEPTION -> status = HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            case AUTHORIZATION_ERROR -> status = HttpStatus.FORBIDDEN;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ApiErrorResponse.ERROR(
                status,
                errorResponse.name(),
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
