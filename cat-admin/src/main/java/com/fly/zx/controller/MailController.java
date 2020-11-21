package com.fly.zx.controller;

import com.fly.zx.common.api.CommonResult;
import com.fly.zx.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Api("邮件管理类")
@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @ApiOperation("验证码管理类")
    @RequestMapping(value = "mail" , method = RequestMethod.POST)
    public CommonResult sendEmail(@RequestParam(value = "userName") String userName,
                                  @RequestParam(value = "email") String email,
                                  HttpServletRequest request){
        String ip = request.getRemoteHost();
        mailService.registerSendMail(userName , email , ip);
        return CommonResult.success();
    }
}
