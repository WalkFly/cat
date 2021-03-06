/**
 * @filename:UserDao 2019年10月16日
 * @project cat-admin  V1.0
 * Copyright(c) 2020 zx Co. Ltd. 
 * All right reserved. 
 */
package com.fly.zx.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.fly.zx.model.User;

/**   
 * @Description:用户数据访问层
 *
 * @version: V1.0
 * @author: zx
 * 
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
	
}
