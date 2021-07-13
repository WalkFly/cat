package com.fly.zx.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.zx.common.api.CommonResult;
import com.fly.zx.dao.CaptchaDao;
import com.fly.zx.dto.CaptchaData;
import com.fly.zx.dto.GetCaptchaResponse;
import com.fly.zx.dto.Payload;
import com.fly.zx.dto.XZResult;
import com.fly.zx.model.Captcha;
import com.fly.zx.service.XZHpvService;
import com.fly.zx.util.HttpsClientUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zx
 * @date 2021/7/9 13:50
 */
@RestController
@RequestMapping(value = "XZ")
public class XZHPVController {
    @Autowired
    private CaptchaDao captchaDao;

    @Autowired
    private XZHpvService xzHpvService;

    @Autowired
    private HttpsClientUtil httpsClientUtil;

    /**
     * 生成对应的图片
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "testBase" , method = RequestMethod.GET)
    public CommonResult testBase() throws IOException {
        List<Captcha> captchaList = captchaDao.selectList(null);
        int i = 0;
        for(Captcha captcha : captchaList){
            i = i+1;
            byte[] bytes = Base64Utils.decodeFromString(captcha.getData());
            File file = new File("C:\\Users\\soyea\\Desktop\\地铁图\\data"+i+".png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        }

//
//         Captcha captcha= captchaDao.selectById(3);
//
//            byte[] bytes = Base64Utils.decodeFromString(captcha.getData());
//            File file = new File("C:\\Users\\soyea\\Desktop\\地铁图\\data"+3+".png");
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            fileOutputStream.write(bytes);
//            fileOutputStream.flush();
//            fileOutputStream.close();

        return  CommonResult.success();
    }

    /**
     * 伪造返回验证码
     * @return
     */
    @RequestMapping(value = "HandlerSubscribe.ashx" , method = RequestMethod.GET)
    public GetCaptchaResponse HandlerSubscribe(){
        QueryWrapper<Captcha> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("y"  , -1);
        queryWrapper.last("limit 1");
        Captcha captcha = captchaDao.selectOne(queryWrapper);
        GetCaptchaResponse captchaRespons = new GetCaptchaResponse();
        captchaRespons.setDragon(captcha.getData());
        captchaRespons.setTiger(captcha.getTiger());
        captchaRespons.setStatus(0);
        captchaRespons.setMsg("");
        Payload payload = new Payload();
        payload.setX(-1);
        payload.setY(5);
        payload.setSecretKey("");
        captchaRespons.setPayload(payload);
        return captchaRespons;
    }

    /**
     * 记录验证码的正确返回值
     * @param x
     * @return
     */
    @RequestMapping(value = "CaptchaVerify" , method = RequestMethod.GET)
    public CommonResult CaptchaVerify(@RequestParam(value = "x") Integer x){
        QueryWrapper<Captcha> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("y"  , -1);
        queryWrapper.last("limit 1");
        Captcha captcha = captchaDao.selectOne(queryWrapper);
        captcha.setY(x);
        captchaDao.updateById(captcha);
        return CommonResult.success();
    }

    /**
     * 校验验证码，如果存在则校验验证码的值，如果不存在则插入数据库
     * @return
     */
    @RequestMapping(value = "checkCaptcha" , method = RequestMethod.GET)
    public CommonResult verify() throws InterruptedException {
        int newNum = 0;
        int erroNum = 0;
        int success = 0;
        String url = "";
        Map heardMap = new HashMap();
        heardMap.put("Host" , "cloud.cn2030.com");
        heardMap.put("Connection" , "keep-alive");
        heardMap.put("Cookie" , "ASP.NET_SessionId=ieq2zpjretvcli0cgexu3qim");
        heardMap.put("content-type" , "application/json");
        heardMap.put("zftsl" , "4d3fb8edeb030c3d55b53d635171758d");
        heardMap.put("Accept-Encoding" , "gzip, deflate, br");
        heardMap.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        heardMap.put("Referer", "https://servicewechat.com/wx2c7f0f3c30d99445/75/page-frame.html");
        while (true){
            url = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=GetCaptcha&mxid=xGqkAB1QAAAWZDQB";
            Thread.sleep(5000);
            String s = httpsClientUtil.get(url, heardMap, "utf-8");
            CaptchaData captchaData = JSON.parseObject(s, CaptchaData.class);
            QueryWrapper<Captcha> queryWrapper = new QueryWrapper<>();
            if(null == captchaData){
                System.out.println("请求失败");
                continue;
            }
            queryWrapper.eq("data" , captchaData.getDragon());
            Captcha existCaptcha = captchaDao.selectOne(queryWrapper);
            if(existCaptcha != null){
                System.out.println("找到相同的特征值");
                if(null == existCaptcha.getY()){
                    continue;
                }
                if(existCaptcha.getY().intValue() == -1){
                    System.out.println("该验证码未进行正确X值计算，跳过");
                    continue;
                }
                url = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=CaptchaVerify&token=&x="+existCaptcha.getY()+"&y=5&mxid=xGqkAB1QAAAWZDQB";
                s = httpsClientUtil.get(url, heardMap, "utf-8");
                System.out.println("接口调用返回:"+s);
                if(s == null) {
                    System.out.println("返回null时传递的X"+ existCaptcha.getY());
                }
                XZResult xzResult = JSON.parseObject(s , XZResult.class);
                if(null  == xzResult || null == xzResult.getStatus()){
                    continue;
                }
                //若没有通过验证，则重新生成
                if(xzResult.getStatus().intValue() != 200){
                    System.out.println("验证失败，修改数据库，使其加入下次验证队列");
                    existCaptcha.setY(-1);
                    captchaDao.updateById(existCaptcha);
                    erroNum = erroNum +1;
                }else{
                    success = success+1;
                }
                if(false){
                    break;
                }
            }else{
                //若在库中找不到验证码，则进行新增
                Captcha captcha = new Captcha();
                captcha.setData(captchaData.getDragon());
                captcha.setTiger(captchaData.getTiger());
                captcha.setY(-1);
                if(StringUtil.isNullOrEmpty(captcha.getData())){
                    System.out.println("发现空值");
                }else{
                    captchaDao.insert(captcha);
                    System.out.println("未在数据库中找到验证码，已记录该验证码");
                    newNum = newNum + 1;
                }
            }
            System.out.println("成功数量： "+success + " 验证失败： " + erroNum + " 未记录数量： " + newNum);
        }

        return CommonResult.success();
    }


    /**
     * 抢购流程
     */
    @RequestMapping(value = "testDo" , method = RequestMethod.GET)
    public CommonResult doSomeTest(Integer hospitalId , Integer vaccinesId , String zftsl , String Cookie , String month,
    String birthday, String tel, String sex, String cname, String idcard, String mid, String date){
        xzHpvService.seckill(hospitalId , vaccinesId , zftsl , Cookie , month , birthday , tel , sex , cname ,idcard , mid ,date);
        return CommonResult.success();
    }
}
