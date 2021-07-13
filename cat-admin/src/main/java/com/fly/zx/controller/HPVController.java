package com.fly.zx.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fly.zx.util.HttpsClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zx
 * @date 2020/12/14 15:44
 */

@Api("模拟进行九价HPV抢单")
@RestController
public class HPVController {

    @Autowired
    private HttpsClientUtil httpsClientUtil;

    /**
     * 程序技术器
     */
    private static Integer num = 1;

    @ApiOperation("测试抢单")
    @RequestMapping(value = "test" , method = RequestMethod.GET)
    public Object test() throws IOException {
        String url = "https://cdn-api.hfi-health.com/hzAppMS/seckill/stockQuery?random=0.4049444283053518&id=e952e63b-df67-45fe-a624-094ac46ad486";
        String json = "{\"seckillType\": 1}";
        Map heardMap = new HashMap();
        heardMap.put("Host" , "cdn-api.hfi-health.com");
        heardMap.put("Referer" , "https://cdn-api.hfi-health.com/hpvAppoint/index.html");
        heardMap.put("appType" , "3");
        heardMap.put("remoteChannel" , "health_APP_H5");
        heardMap.put("X-Requested-With" , "XMLHttpRequest");
        heardMap.put("User-Agent" , "Mozilla/5.0 (iPhone; CPU iPhone OS 14_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148JKTNATIVE,appChannel/hzjkt,appVersion-2.9.3-v");
        heardMap.put("appVersion" , "h5");
        heardMap.put("Origin" , "https://cdn-api.hfi-health.com");
        heardMap.put("Content-Length" , "17");
        heardMap.put("Connection" , "keep-alive");
        heardMap.put("timestamp" , "1607913640576");
        heardMap.put("Accept-Language" , "zh-cn");
        heardMap.put("Accept" , "application/json");
        heardMap.put("Content-Type" , "application/json");
        heardMap.put("Accept-Encoding" , "gzip, deflate, br");

        heardMap.put("logTraceId" , "0381b153-2456-459e-acd0-b8e176ca390d");
        heardMap.put("signature" , "76ebb6a847de82c7c899af0edd66d36891365b797c6678690f25ef79588f3288");
        heardMap.put("nonce" , "1fe2b6d7-7864-45f4-988e-18217cf60311");
        heardMap.put("id" , "e952e63b-df67-45fe-a624-094ac46ad486");
        heardMap.put("token" , "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlOTUyZTYzYi1kZjY3LTQ1ZmUtYTYyNC0wOTRhYzQ2YWQ0ODYiLCJleHAiOjE2MDkxMjMxNjB9.gL1ulcLR0qVi4i6iYljHLpNLTJxEC8K3oLSLVYIOg4Y");
        while (true){
            if(doRequest(heardMap , url , json)){
                System.exit(0);
                break;
            }
        }
        return "123";
    }

    /**
     * 进行抢单的模块，返回抢单成功与否，若抢单成功返回true
     * @param heardMap
     * @param url
     * @param json
     * @return
     */
    @Async
    public boolean doRequest(Map heardMap , String url , String json){
        String s = "";
        try {
            heardMap.put("timestamp" , String.valueOf(new Date().getTime()) );
            s = httpsClientUtil.postJson(url , heardMap,json , "utf-8");
            JSONObject jsonObject = (JSONObject) JSON.parse(s);
            String test =  jsonObject.getJSONObject("value").getString("seckillList");
            num ++;
            System.out.println(num+"结果"+s);
            if(test != null ){
                System.out.println("抢单成功！");
                return true;
            }

        } catch (Exception e) {
            num ++;
            System.out.println(num+"结果"+s);
        }
        return false;
    }
}
