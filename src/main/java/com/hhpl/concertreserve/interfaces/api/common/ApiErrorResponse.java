package com.hhpl.concertreserve.interfaces.api.common;

import org.springframework.http.HttpStatus;

public record ApiErrorResponse(int status, String code,String message) {

    public static ApiErrorResponse ERROR(HttpStatus status, String code, String message) {
        return new ApiErrorResponse(status.value(), code, message);
    }

}