package com.yanfeitech.application.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVO extends BaseVO implements Serializable {
	private static final long serialVersionUID = 6575371535822070449L;

	/**
	 * 用户名
	 */// 账号
	private String username;

	// 密码
	private String password;

	// 真实姓名
	private String name;

	// 角色
	private String role;

	public void obito(){

	}


}
