package com.yanfeitech.application.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 6575371535822070449L;

	// 课程id
	private String unitId;

	// 章节名称
	private String chapterName;

	// 章节序号
	private Integer chapterNo;

	// 课时列表
	private List<CoursewareVO> coursewareList;
}
