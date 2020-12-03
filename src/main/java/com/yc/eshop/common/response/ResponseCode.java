package com.yc.eshop.common.response;

/**
 * @author 余聪
 * @date 2020/10/16
 */
public enum ResponseCode {

    //成功
    OK(200, "success"),
    //错误
    ERROR(500, "server internal error"),
    //请求失败
    BAD_REQUEST(400, "Bad Request"),
    //未登录
    UNAUTHORIZED(401, "Unauthorized"),
    //禁止访问
    FORBIDDEN(403, "Forbidden"),
    //资源未找到
    NOT_FOUND(404, "Not Found"),
    //方法不允许
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    //服务器无法根据客户端请求的内容特性完成请求
    NOT_ACCEPTABLE(406, "Not Acceptable")
    ;

    private final int value;
    private final String message;

    ResponseCode(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

}
