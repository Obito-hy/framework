package com.yanfeitech.application.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(indexes = {@Index(columnList = "id")})
public class ClassCourse extends BaseEntity {
	
	// 对应 班级 id
	private String  schoolClassId;
	
	//对应 课程 id
	private String  courseId;

}
