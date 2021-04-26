package com.yanfeitech.application.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * Title: BaseEntity
 * </p>
 * <p>
 * Description: 实体基础类，所有数据库实体类都要继承自该类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public abstract class BaseVO {
	protected String id;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	protected Date createTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	protected Date updateTime;
}
