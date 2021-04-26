package com.yanfeitech.application.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@ApiModel
public class NoticeVO  extends BaseVO implements Serializable {
	//所 属  班级
	@ApiModelProperty(value = "所属班级")
	private String schoolClassId;

	//课程 编号
	@ApiModelProperty(value = "课程编号")
	private String courseId;

	//消息 类型
	@ApiModelProperty(value = "消息类型")
	private Integer type;

	//下发时间
	@ApiModelProperty(value = "下发时间")
	private Date sendTime;

	//详细 消息
	@ApiModelProperty(value = "消息内容")
	@Column(length = 255)
	private String details;

	//状态
	@ApiModelProperty(value = "消息状态")
	private Integer status;

	//班级 编号
	@ApiModelProperty(value = "班级编号")
	private String classNumbering;

	//课程 编号
	@ApiModelProperty(value = "课程编号")
	private String courseNumbering;
	
	//班级 编号
	@ApiModelProperty(value = "班级id集合")
	private List<String> schoolClassIdList;
	
	
}
