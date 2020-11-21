/**
 * @filename:Mail 2019年10月16日
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
 * @Description:邮箱实体类
 * 
 * @version: V1.0
 * @author: zx
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Mail extends Model<Mail> {

	private static final long serialVersionUID = 1605969291288L;
	
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(name = "id" , value = "")
	private Long id;
    
	@ApiModelProperty(name = "registerName" , value = "通过邮件注册的用户名")
	private String registerName;
    
	@ApiModelProperty(name = "address" , value = "发送地址")
	private String address;
    
	@ApiModelProperty(name = "title" , value = "邮件的主题内容")
	private String title;
    
	@ApiModelProperty(name = "content" , value = "发送内容")
	private String content;
    
	@ApiModelProperty(name = "type" , value = "1.注册")
	private Integer type;
    
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@ApiModelProperty(name = "createTime" , value = "发送时间")
	private Date createTime;
    
	@ApiModelProperty(name = "ip" , value = "请求ip")
	private String ip;
    

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
