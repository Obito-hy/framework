package com.yanfeitech.application.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yanfeitech.application.common.util.CommonUtil;
import com.yanfeitech.application.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 
 * <p>
 * Title: JwtUser
 * </p>
 * <p>
 * Description: 用户登录中springsecurity返回的用户表查询对象
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class JwtUser extends User implements UserDetails {

	private static final long serialVersionUID = 1872316361979805711L;

	/*
	 * 用户权限，用于@PreAuthorize检验
	 */
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public void setRole(String role) {
		super.setRole(role);
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String r : CommonUtil.stringConvertStringList(role)) {
			authorities.add(new SimpleGrantedAuthority(r));
		}
	}

	// 账号是否未过期，默认是false，记得要改一下
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 账号是否未锁定，默认是false，记得也要改一下
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 账号凭证是否未过期，默认是false，记得还要改一下
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 这个有点抽象不会翻译，默认也是false，记得改一下
	@Override
	public boolean isEnabled() {
		return true;
	}
}
