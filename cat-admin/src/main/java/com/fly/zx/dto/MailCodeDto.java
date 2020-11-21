package com.fly.zx.dto;
import lombok.Data;

@Data
public class MailCodeDto {
    private String userName;
    private String code;
    private Long lastSendTime;
}
