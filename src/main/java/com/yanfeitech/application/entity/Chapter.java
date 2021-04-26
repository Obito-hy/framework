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
public class Chapter extends BaseEntity {
	// 章节表ID
	private String unitId;

	// 课时序号
	private Integer chapterNo;

	// 课时名称
	private String chapterName;
}
