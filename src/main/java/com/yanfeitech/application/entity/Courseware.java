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
public class Courseware extends BaseEntity {

	// 课时表id
	private String chapterId;

	// 标题
	private String title;

	// 类型
	private String type;

	// 课件编号
	private Integer coursewareNo;

}
