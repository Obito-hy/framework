package com.yanfeitech.application.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
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
 * @date 2021/4/25 10:51
 */
@Entity
@Getter
@Setter
@Table(indexes = { @Index(columnList = "id") })
public class Student extends BaseEntity {
}
