package com.yanfeitech.application.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * TODO
 *
 * @author Obito
 * @version 1.0
 * @date 2021/4/25 10:52
 */
@Entity
@Getter
@Setter
@Table(indexes = {@Index(columnList = "id")})
public class StudentClass extends BaseEntity {
    //学生id
    private String studentId;

    //教室 编号
    private String classroomNumber;
}
