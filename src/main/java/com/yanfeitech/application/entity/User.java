package com.yanfeitech.application.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * Title: User
 * </p>
 * <p>
 * Description: 用户实体类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Entity
@Getter
@Setter
@Table(indexes = { @Index(columnList = "id") })
public class User extends BaseEntity {

	// 账号
	private String username;

	// 密码
	private String password;

	// 真实姓名
	private String name;

	// 角色
	private String role;
}
