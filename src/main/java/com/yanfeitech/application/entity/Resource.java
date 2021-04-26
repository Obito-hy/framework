package com.yanfeitech.application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = { @Index(columnList = "id") })
public class Resource extends BaseEntity {
	// 课件id
	private String coursewareId;

	// 资源名称
	private String resourceName;

	// 类型
	private String type;

	// 图表类型 针对类型为图表的资源
	private String graphType;

	// 图表类型 针对类型为图表的资源
	private String graphElseType;

	// 序号
	private Integer resourceNo;

	// 内容描述
	@Column(length = 2047)
	private String contentDetail;

	// 内容描述
	@Column(length = 2047)
	private String contentDescription;

	// 文件
	private String fileUrl;

	// 动作步骤
	@Column(length = 2047)
	private String actionSteps;

	// 动作难点
	@Column(length = 2047)
	private String actionPoint;

}
