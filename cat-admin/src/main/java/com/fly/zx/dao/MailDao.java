/**
 * @filename:MailDao 2019年10月16日
 * @project cat-admin  V1.0
 * Copyright(c) 2020 zx Co. Ltd. 
 * All right reserved. 
 */
package com.fly.zx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.fly.zx.model.Mail;
import org.springframework.stereotype.Repository;

/**   
 * @Description:邮件访问层
 *
 * @version: V1.0
 * @author: zx
 * 
 */
@Mapper
@Repository
public interface MailDao extends BaseMapper<Mail> {
	
}
