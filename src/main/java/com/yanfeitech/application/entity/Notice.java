package com.yanfeitech.application.entity;

import com.yanfeitech.application.enums.NoticeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

/**
 * TODO
 *
 * @author Obito
 * @version 1.0
 * @date 2021/4/21 9:12
 */

@Entity
@Data
@ApiModel(value="notice对象",description="消息对象notice")
@Table(indexes = {@Index(columnList = "id")})
public class Notice extends BaseEntity {

    //所 属  班级
    @ApiModelProperty(value="班级编号",name="school_class_id")
    private String schoolClassId;

    //课程 编号
    @ApiModelProperty(value="课程编号",name="course_id")
    private String courseId;

    //消息 类型
    @ApiModelProperty(value = "消息类型",name="type")
    private Integer type;

    //下发时间
    @ApiModelProperty(value = "下发时间",name="sent_time")
    private Date sendTime;

    //详细 消息
    @ApiModelProperty(value = "消息内容",name = "details")
    @Column(length = 255)
    private String details;

    //状态
    @ApiModelProperty(value = "消息状态",name = "status")
    private Integer status;
}
