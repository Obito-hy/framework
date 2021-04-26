package com.yanfeitech.application.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 6575371535822070449L;

	// 课程id
	private String courseId;

	// 章节名称
	private String unitName;

	// 章节序号
	private Integer unitNo;

	// 课时列表
	private java.util.List<ChapterVO> chapterList;
}
