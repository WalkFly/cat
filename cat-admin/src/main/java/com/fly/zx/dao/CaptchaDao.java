/**
 * @filename:CaptchaDao 2019年10月16日
 * @project soyea  V1.0
 * Copyright(c) 2020 zx Co. Ltd. 
 * All right reserved. 
 */
package com.fly.zx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.fly.zx.model.Captcha;

/**   
 * @Description:TODO(API应用KEY数据访问层)
 *
 * @version: V1.0
 * @author: zx
 * 
 */
@Mapper
public interface CaptchaDao extends BaseMapper<Captcha> {
	
}
