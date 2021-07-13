package com.fly.zx.service;

import com.fly.zx.dto.GetDateResponse;
import com.fly.zx.dto.GetVaccinesIdResponse;
import com.fly.zx.dto.VaccinesInfoDto;

/**
 * TODO
 *
 * @author zx
 * @date 2021/7/13 13:34
 */
public interface XZHpvService {

    /**
     * 秒杀
     */
    void seckill(Integer hospitalId, Integer vaccinesId, String zftsl, String Cookie , String month , String birthday, String tel, String sex, String cname, String idcard, String mid, String date);

    /**
     * 获取疫苗日期
     */
    GetDateResponse getVaccinesDate(Integer hospitalId , Integer vaccinesId ,    String zftsl , String Cookie , String month);



    /**
     * 获取疫苗信息
     * @param hospitalId
     * @param vaccinesId
     * @param zftsl
     * @param Cookie
     * @param month
     * @return
     */
    GetVaccinesIdResponse VaccinesInfo(Integer hospitalId, Integer vaccinesId, String zftsl, String Cookie , String month);

    /**
     * 获取验证码并提交验证码
     */
    String getAndSumbimitCatpcha( String zftsl, String Cookie,  String mid);

    /**
     * 提交个人信息
     */
    void submitPersonInfo(String zftsl , String Cookie , String birthday , String tel ,String sex ,String cname , String idcard , String mid , String date , Integer vaccinesId, String guid);
}
