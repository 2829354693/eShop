package com.yc.eshop.advice;

import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 余聪
 * @date 2020/12/3
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleUnknownException(Exception e) {
        return ApiResponse.failure(ResponseCode.ERROR, e.getMessage());
    }
}
