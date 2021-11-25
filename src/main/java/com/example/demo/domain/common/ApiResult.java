package com.example.demo.domain.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@RequiredArgsConstructor
public class ApiResult<T> {
    @ApiModelProperty
    private final T data;
    @ApiModelProperty
    private final String error;
    @ApiModelProperty
    private final HttpStatus httpStatus;

    public static <T> ResponseEntity<ApiResult<T>> succeed(T data) {
        return succeed(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResult<T>> succeed(T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResult<>(data, "", httpStatus), httpStatus);
    }

    public static <T> ResponseEntity<ApiResult<T>> succeed(T data, HttpHeaders httpHeaders, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResult<>(data, "", httpStatus), httpHeaders, httpStatus);
    }

    public static ResponseEntity<ApiResult<?>> failed(Throwable throwable, HttpStatus httpStatus) {
        return failed(throwable.getMessage(), httpStatus);
    }

    public static ResponseEntity<ApiResult<?>> failed(String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ApiResult<>("", message, httpStatus), httpStatus);

    }

    public static ResponseEntity<ApiResult<?>> failed(Throwable throwable) {
        return failed(throwable.getMessage());
    }

    public static ResponseEntity<ApiResult<?>> failed(String message) {
        return failed(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
