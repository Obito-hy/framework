package com.yanfeitech.application.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yanfeitech.application.entity.User;

/**
 * 
 * <p>
 * Title: UserIDAuditorBean
 * </p>
 * <p>
 * Description:使用上下文中的用户id，提供给JPA，用于数据库表操作人createby与updateby
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Configuration
public class UserIDAuditorBean implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx == null) {
			return null;
		}
		if (ctx.getAuthentication() == null) {
			return null;
		}
		if (ctx.getAuthentication().getPrincipal() == null) {
			return null;
		}
		if (ctx.getAuthentication() instanceof AnonymousAuthenticationToken) {
			return null;
		}
		if (ctx.getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
			Object principal = ctx.getAuthentication().getPrincipal();
			User user = (User) principal;
			if (user != null) {
				return Optional.of(user.getId());
			}
		}
		return null;
	}
}