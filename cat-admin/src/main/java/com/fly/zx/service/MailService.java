package com.fly.zx.service;

public interface MailService {

    /**
     * 发送邮件
     * @param address
     * @param title
     * @param content
     */
    void sendMail(String address , String title , String content);
}
