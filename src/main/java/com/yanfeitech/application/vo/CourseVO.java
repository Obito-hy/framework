package com.yanfeitech.application.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 6575371535822070449L;

	// 课程名称
	private String courseName;

	// 出版社
	private String textbookVersion;

	// 章节列表
	private List<UnitVO> unitList;

	//教学大纲
	private List<FileVO> outlineFileList;

	//教学计划
	private List<FileVO> planFileList;

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

	//教室 编号
	private String classroomNumber;
}
