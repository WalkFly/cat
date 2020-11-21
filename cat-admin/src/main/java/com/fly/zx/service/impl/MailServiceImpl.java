package com.fly.zx.service.impl;

import com.alibaba.fastjson.JSON;
import com.fly.zx.common.ApiResult;
import com.fly.zx.common.exception.SystemException;
import com.fly.zx.common.service.RedisService;
import com.fly.zx.dto.MailCodeDto;
import com.fly.zx.service.MailService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private RedisService redisService;

    private static final String title = "cat论坛注册";

    private static final String content = "欢迎您接受内测，验证码为:";


    @Override
    public void registerSendMail(String userName , String addresss) {
        String code = redisService.get(userName);
        MailCodeDto mailCodeDto;
        if(!StringUtil.isNullOrEmpty(code)){
            mailCodeDto = JSON.parseObject(code, MailCodeDto.class);
            if(new Date().getTime() - mailCodeDto.getLastSendTime() < 60000){
                throw new SystemException(ApiResult.MAIL_CODE_IN_ONE_MIN);
            }
        }
        Random random = new Random();
        int randomCode = random.nextInt(999999);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置收件人，寄件人
        simpleMailMessage.setTo(new String[]{"13567791995@163.com" , addresss});
        simpleMailMessage.setFrom("13567791995@163.com");
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(content+randomCode);
        // 发送邮件
        mailSender.send(simpleMailMessage);
        mailCodeDto = new MailCodeDto();
        mailCodeDto.setLastSendTime(new Date().getTime());
        mailCodeDto.setUserName(userName);
        mailCodeDto.setCode(String.valueOf(randomCode));
        redisService.set(userName , JSON.toJSONString(mailCodeDto));
    }
}
