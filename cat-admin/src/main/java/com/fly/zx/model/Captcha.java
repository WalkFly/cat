/**
 * @filename:Captcha 2019年10月16日
 * @project soyea  V1.0
 * Copyright(c) 2020 zx Co. Ltd. 
 * All right reserved. 
 */
package com.fly.zx.model;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**   
 * @Description:TODO(API应用KEY实体类)
 * 
 * @version: V1.0
 * @author: zx
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Captcha extends Model<Captcha> {

	private static final long serialVersionUID = 1625818390330L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Integer id;
    
	@ApiModelProperty(name = "data" , value = "请求内容")
	private String data;
    
	@ApiModelProperty(name = "y" , value = "验证码答案")
	private Integer y;
    
	@ApiModelProperty(name = "tiger" , value = "")
	private String tiger;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
