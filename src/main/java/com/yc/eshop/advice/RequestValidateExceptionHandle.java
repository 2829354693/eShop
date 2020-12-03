package com.yc.eshop.advice;

import com.yc.eshop.common.entity.Error;
import com.yc.eshop.common.response.ApiResponse;
import com.yc.eshop.common.response.ResponseCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author 余聪
 * @date 2020/12/2
 */

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestValidateExceptionHandle {

    /**
     * 效验错误拦截处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        String message = "";
        if (result.hasErrors()) {
            List<FieldError> allErrors = result.getFieldErrors();
            if (allErrors.size() > 0) {
                message = allErrors.get(0).getDefaultMessage();
            }
        }
        return ApiResponse.failure(ResponseCode.BAD_REQUEST, "".equals(message) ? "请正确填写信息" : message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.failure(ResponseCode.BAD_REQUEST, e.getMessage());
    }
}
