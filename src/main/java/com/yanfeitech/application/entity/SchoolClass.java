package com.yanfeitech.application.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * TODO
 *
 * @author Obito
 * @version 1.0
 * @date 2021/4/21 9:59
 */
@Entity
@Setter
@Getter
@ToString
@Table(indexes = {@Index(columnList = "id")})
public class SchoolClass extends BaseEntity{

    // 班级 编号
    private String numbering;


}
