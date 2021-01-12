package com.yc.eshop.common.response;

import java.io.Serializable;

/**
 * @author 余聪
 * @date 2020/10/16
 */
public class ApiResponse<T> implements Serializable {

    private Integer code;

    private Long total;

    private String message;

    private T data;

    public static ApiResponse<Void> ok() {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ResponseCode.OK.getValue());
        apiResponse.setMessage(ResponseCode.OK.getMessage());
        return apiResponse;
    }

    public static ApiResponse<Void> ok(String message) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ResponseCode.OK.getValue());
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ResponseCode.OK.getValue());
        apiResponse.setMessage(ResponseCode.OK.getMessage());
        apiResponse.setData(data);
        return apiResponse;
    }

    public static <T> ApiResponse<T> ok(T data, Long total) {
        ApiResponse<T> apiResponse = ApiResponse.ok(data);
        apiResponse.setTotal(total);
        return apiResponse;
    }

    public static ApiResponse<Void> failure(ResponseCode responseCode) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(responseCode.getValue());
        apiResponse.setMessage(responseCode.getMessage());
        return apiResponse;
    }

    public static ApiResponse<Void> failure(ResponseCode responseCode, String message) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(responseCode.getValue());
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static <T> ApiResponse<T> failure(ResponseCode responseCode, T data, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(responseCode.getValue());
        apiResponse.setData(data);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public static ApiResponse<Void> failure(String message) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
