package com.yanfeitech.application.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public abstract class BaseEntity {
	// 主键
	@Id
	@Column(unique = true, length = 63)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	protected String id;

	// 创建时间
	@CreatedDate
	@Column(updatable = false)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	protected Date createTime;

	// 更新时间
	@LastModifiedDate
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	protected Date updateTime;

	@CreatedBy
	@JSONField(serialize = false)
	@Column(updatable = false)
	protected String createBy;

	@LastModifiedBy
	@JSONField(serialize = false)
	protected String lastmodifiedBy;
}
