package com.fly.zx.service.impl;

import com.fly.zx.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Override
    public void sendMail(String address, String title, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人
        simpleMailMessage.setTo(new String[]{"2501902696@qq.com", "1587072557@qq.com"});
        simpleMailMessage.setFrom("17346850383@163.com");
        simpleMailMessage.setSubject("Spring Boot Mail 邮件测试【文本】");
        simpleMailMessage.setText("这里是一段简单文本。");
        // 发送邮件
        mailSender.send(simpleMailMessage);

        System.out.println("邮件已发送");

    }
}
