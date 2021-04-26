package com.yanfeitech.application.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(indexes = { @Index(columnList = "id") })
public class Course extends BaseEntity {
	
	// 课程名
	private String courseName;

	// 教材版本
	private String textbookVersion;
	
	//封面图
	private String posterUrl;

	//课程 编号
	private String numbering;

	//班级
	private String schoolCalssId;

	//总课时数
	private String totalClassHours;

	//已备课课时数
	private String lessonPreparationOk;

	//教学大纲描述
	private String outlineDescription;

	//教学计划描述
	private String planDescription;

	//教师id
	private String teacherId;



}
