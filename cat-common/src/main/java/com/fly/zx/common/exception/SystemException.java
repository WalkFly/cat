package com.fly.zx.common.exception;

import com.fly.zx.common.api.IErrorCode;

/**
 * 自定义API异常
 * Created by macro on 2020/2/27.
 */
public class SystemException extends RuntimeException {
    private IErrorCode errorCode;

    public SystemException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
