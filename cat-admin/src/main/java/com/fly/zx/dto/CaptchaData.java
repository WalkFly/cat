package com.fly.zx.dto;

import lombok.Data;

/**
 *
 * @author zx
 * @date 2021/7/9 14:11
 */
@Data
public class CaptchaData {
    private String dragon;
    private Object payload;
    private Integer status;
    private String tiger;
    private String msg;
}
