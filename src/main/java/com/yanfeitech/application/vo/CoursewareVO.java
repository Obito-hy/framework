package com.yanfeitech.application.vo;

import java.io.Serializable;
import java.util.List;

import com.yanfeitech.application.entity.Resource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoursewareVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 6575371535822070449L;

	// 课时表id
	private String chapterId;

	// 标题
	private String title;

	// 类型
	private String type;

	// 课件编号
	private Integer coursewareNo;

	// 资源列表
	private List<Resource> resourceList;
}
