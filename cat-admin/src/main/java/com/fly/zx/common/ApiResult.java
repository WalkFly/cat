package com.fly.zx.common;

import com.fly.zx.common.api.IErrorCode;

public enum  ApiResult implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    FORBIDDEN(403, "没有相关权限"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    MAIL_CODE_IN_ONE_MIN(10000, "请勿在一分钟内请求");
    private long code;
    private String message;

    private ApiResult(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
