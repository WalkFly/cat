/**
 * @filename:User 2019年10月16日
 * @project cat-admin  V1.0
 * Copyright(c) 2020 zx Co. Ltd. 
 * All right reserved. 
 */
package com.fly.zx.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**   
 * @Description:用户实体类
 * 
 * @version: V1.0
 * @author: zx
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

	private static final long serialVersionUID = 1605933547877L;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "创建时间")
	private Date createTime;
    
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Long id;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "lastLoginTime" , value = "上次登录时间")
	private Date lastLoginTime;
    
	@ApiModelProperty(name = "mail" , value = "邮箱")
	private String mail;
    
	@ApiModelProperty(name = "password" , value = "密码")
	private String password;
    
	@ApiModelProperty(name = "state" , value = "状态 0 删除 1 禁用 2有效")
	private Integer state;
    
	@ApiModelProperty(name = "userName" , value = "用户名")
	private String userName;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
