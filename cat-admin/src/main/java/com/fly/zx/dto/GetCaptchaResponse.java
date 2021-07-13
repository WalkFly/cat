package com.fly.zx.dto;

import lombok.Data;

/**
 *
 * @author zx
 * @date 2021/7/12 15:34
 */
@Data
public class GetCaptchaResponse {
    private String dragon;
    private String tiger;
    private Object payload;
    private Integer status;
    private String msg;
}
