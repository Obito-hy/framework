package com.yanfeitech.application.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = { @Index(columnList = "id") })
public class Unit extends BaseEntity {
	// 课程id
	private String courseId;

	// 章节名
	private String unitName;

	// 章节序号
	private Integer unitNo;
}
