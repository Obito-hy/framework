package com.yanfeitech.application.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * Title: DictItem
 * </p>
 * <p>
 * Description: 字典实体类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Entity
@Getter
@Setter
@Table(indexes = { @Index(columnList = "id") })
public class DictItem extends BaseEntity {

	/**
	 * 字典类型
	 */
	private String type;
	/**
	 * 字典编码
	 */
	private String code;
	/**
	 * 字典名称
	 */
	private String name;
	/**
	 * 排序
	 */
	private String orderNo;
}
