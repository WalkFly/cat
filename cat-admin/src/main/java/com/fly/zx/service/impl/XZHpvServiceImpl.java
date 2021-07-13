package com.fly.zx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.zx.dao.CaptchaDao;
import com.fly.zx.dto.*;
import com.fly.zx.model.Captcha;
import com.fly.zx.service.XZHpvService;
import com.fly.zx.util.HttpsClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zx
 * @date 2021/7/13 14:00
 */
@Service
public class XZHpvServiceImpl implements XZHpvService {
    private String getDateUrl = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx";
    private String getVaccinesIdUrl = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx";
    private String getCaptchaUrl = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx";
    private String submitCaptchaUrl = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx";
    private String submitPerson = "https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx";
    private static Map headMap;

    @Autowired
    private HttpsClientUtil httpsClientUtil;

    @Autowired
    private CaptchaDao captchaDao;

    @Override
    public void seckill(Integer hospitalId, Integer vaccinesId, String zftsl, String Cookie , String month , String birthday, String tel, String sex, String cname, String idcard, String mid, String date) {
        Long start = new Date().getTime();
        if(null == mid){
            //获取日期
            long dateStart = new Date().getTime();
            GetDateResponse vaccinesDate = this.getVaccinesDate(hospitalId, vaccinesId, zftsl, Cookie, month);
            System.out.println(JSON.toJSONString(vaccinesDate));
            System.out.println("获取日期耗费" + (new Date().getTime() - dateStart) +"ms");

            dateStart = new Date().getTime();
            //获取疫苗id
            List<DateDto> list = vaccinesDate.getList();
            if(null == list || list.size() == 0){
                System.out.println("争抢失败");
                return;
            }
            DateDto dateDto = list.get(list.size() - 1);
            GetVaccinesIdResponse getVaccinesIdResponse = this.VaccinesInfo(hospitalId, vaccinesId, zftsl, Cookie, dateDto.getDate());
            System.out.println(JSON.toJSONString(getVaccinesIdResponse));


            //获取mid
            List<VaccinesInfoDto> vaccinesInfoDtoList = getVaccinesIdResponse.getList();
            if(null == vaccinesInfoDtoList || vaccinesInfoDtoList.size() == 0){
                System.out.println("争抢失败");
                return;
            }
            VaccinesInfoDto vaccinesInfoDto = vaccinesInfoDtoList.get(vaccinesInfoDtoList.size() - 1);
            mid = vaccinesInfoDto.getMxid();
            date = dateDto.getDate();

            System.out.println("获取疫苗mxid耗费" + (new Date().getTime() - dateStart) +"ms");
        }



        //获取guid
        long dateStart = new Date().getTime();
        String guid = this.getAndSumbimitCatpcha(zftsl, Cookie, mid);
        System.out.println(guid);
        System.out.println("获取验证码并提交耗费" + (new Date().getTime() - dateStart) +"ms");
        //提交信息
        dateStart = new Date().getTime();
        this.submitPersonInfo(zftsl , Cookie , birthday , tel , sex , cname , idcard , mid , date , vaccinesId , guid);
        System.out.println("提交个人信息耗时" + (new Date().getTime() - dateStart) +"ms");
        System.out.println("共计耗时：" + (new Date().getTime() - start) + "ms");
    }

    private static Map getHeadMap(String zftsl, String Cookie){
        if(headMap == null){
            headMap = new HashMap();
            headMap.put("Host" , "cloud.cn2030.com");
            headMap.put("Connection" , "keep-alive");
            headMap.put("content-type" , "application/json");
            headMap.put("Accept-Encoding" , "gzip, deflate, br");
            headMap.put("User-Agent" , "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
            headMap.put("Referer", "https://servicewechat.com/wx2c7f0f3c30d99445/75/page-frame.html");
            headMap.put("zftsl" , zftsl);
            headMap.put("Cookie" , Cookie);
            return headMap;
        }
        headMap.put("zftsl" , zftsl);
        headMap.put("Cookie" , Cookie);
        return headMap;
    }

    @Override
    public GetDateResponse getVaccinesDate(Integer hospitalId, Integer vaccinesId, String zftsl, String Cookie , String month) {
        Map headMap = XZHpvServiceImpl.getHeadMap(zftsl, Cookie);
        Map param = new HashMap();
        param.put("id" , String.valueOf(hospitalId));
        param.put("pid" , String.valueOf(vaccinesId));
        param.put("month" , month);
        param.put("act" , "GetCustSubscribeDateAll");
        while (true){
            String s = httpsClientUtil.getMap(getDateUrl, headMap, param, "utf-8");
            if(s == null ){
                continue;
            }
            GetDateResponse getDateResponse = JSON.parseObject(s, GetDateResponse.class);
            if(getDateResponse == null || getDateResponse.getStatus() == null ||getDateResponse.getStatus().intValue() != 200){
                continue;
            }
            return getDateResponse;
        }

    }


    @Override
    public GetVaccinesIdResponse VaccinesInfo(Integer hospitalId, Integer vaccinesId, String zftsl, String Cookie, String scdate) {
        Map headMap = XZHpvServiceImpl.getHeadMap(zftsl, Cookie);
        Map param = new HashMap();
        param.put("id" , String.valueOf(hospitalId));
        param.put("pid" , String.valueOf(vaccinesId));
        param.put("scdate" , scdate);
        param.put("act" , "GetCustSubscribeDateDetail");
        while (true){
            String s = httpsClientUtil.getMap(getVaccinesIdUrl, headMap, param, "utf-8");
            System.out.println(s);
            if(s == null ){
                continue;
            }
            GetVaccinesIdResponse getVaccinesIdResponse = JSON.parseObject(s, GetVaccinesIdResponse.class);
            if(getVaccinesIdResponse == null || getVaccinesIdResponse.getStatus() == null ||getVaccinesIdResponse.getStatus().intValue() != 200){
                continue;
            }
            return getVaccinesIdResponse;
        }
    }

    @Override
    public String getAndSumbimitCatpcha( String zftsl, String Cookie,  String mid) {
        Map headMap = XZHpvServiceImpl.getHeadMap(zftsl, Cookie);
        Map param = new HashMap();
        param.put("act" , "GetCaptcha");
        param.put("mxid" , mid);
        XZResult xzResult = new XZResult();
        while (true){
            String s = httpsClientUtil.getMap(getCaptchaUrl, headMap, param, "utf-8");
            if(s == null ){
                continue;
            }
            CaptchaData captchaData = JSON.parseObject(s, CaptchaData.class);
            if(captchaData == null || captchaData.getStatus() == null ||captchaData.getStatus().intValue() != 0){
                continue;
            }
            QueryWrapper<Captcha> queryWrapper = new QueryWrapper<>();
            if(null == captchaData){
                System.out.println("请求失败");
                continue;
            }
            queryWrapper.eq("data" , captchaData.getDragon());
            Captcha existCaptcha = captchaDao.selectOne(queryWrapper);
            if(existCaptcha != null) {
                System.out.println("找到相同的特征值");
                if (null == existCaptcha.getY()) {
                    continue;
                }

                if (existCaptcha.getY().intValue() == -1) {
                    System.out.println("该验证码未进行正确X值计算，跳过");
                    continue;
                }
                param.clear();
                param.put("act" , "CaptchaVerify");
                param.put("token" , "");
                param.put("x" , String.valueOf(existCaptcha.getY()));
                param.put("y" , "5");
                param.put("mxid", mid);
                while (true){
                    s = httpsClientUtil.getMap(submitCaptchaUrl, headMap , param, "utf-8");
                    System.out.println("接口调用返回:" + s);
                    if (s == null) {
                        System.out.println("返回null时传递的X" + existCaptcha.getY());
                        continue;
                    }else{
                        break;
                    }
                }
                xzResult = JSON.parseObject(s, XZResult.class);
                if (null == xzResult || null == xzResult.getStatus()) {
                    continue;
                }
            }else{
                continue;
            }
            return xzResult.getGuid();
        }
    }

    @Override
    public void submitPersonInfo(String zftsl , String Cookie , String birthday, String tel, String sex, String cname, String idcard, String mid, String date, Integer vaccinesId, String guid) {
        Map headMap = XZHpvServiceImpl.getHeadMap(zftsl, Cookie);
        Map param = new HashMap<String , String>();
        param.put("act" , "Save20");
        param.put("birthday" , birthday);
        param.put("tel" , tel);
        param.put("sex" , String.valueOf(sex));
        param.put("cname" , cname);
        param.put("doctype" , "1");
        param.put("idcard" , idcard);
        param.put("mxid" , mid);
        param.put("date" , date);
        param.put("pid" , String.valueOf(vaccinesId));
        param.put("Ftime" , "1");
        param.put("guid" , guid);

        while (true){
            String s = httpsClientUtil.getMap(submitPerson, headMap, param, "utf-8");
            if(s == null ){
                continue;
            }else{
                System.out.println(s);
                return;
            }
        }
    }
}
