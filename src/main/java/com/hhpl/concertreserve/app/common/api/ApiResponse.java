package com.hhpl.concertreserve.app.common.api;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(int status, T data, String message) {

    public static ApiResponse<?> OK() {
        return new ApiResponse<>(HttpStatus.OK.value(), null, "Success");
    }

    public static <T> ApiResponse<T> OK(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, "Success");
    }

    public static <T> ApiResponse<T> OK(T data, String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, message);
    }


}
