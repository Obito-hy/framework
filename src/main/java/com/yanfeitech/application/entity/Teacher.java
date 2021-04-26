package com.yanfeitech.application.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * TODO
 *
 * @author Obito
 * @version 1.0
 * @date 2021/4/25 10:50
 */
@Entity
@Data
@Table(indexes = {@Index(columnList = "id")})
public class Teacher extends BaseEntity {

}
