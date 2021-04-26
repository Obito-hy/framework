package com.yanfeitech.application.common.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.yanfeitech.application.entity.User;

/**
 * 
 * <p>
 * Title: LoginUserUtil
 * </p>
 * <p>
 * Description: 当前用户工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class LoginUserUtil {
	/**
	 * 
	 * <p>
	 * Title: getLoginUser
	 * </p>
	 * <p>
	 * Description: 获取当前登录的用户对象
	 * </p>
	 * 
	 * @return User
	 * @author zhudelin
	 * @date 2020年11月24日
	 * @version 1.0
	 */
	public static User getLoginUser() {
		/**
		 * SecurityContextHolder.getContext()获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
		 * 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象) 获取当前认证了的
		 * principal(当事人),或者 request token (令牌) 如果没有认证，会是 null
		 */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 有登陆用户就返回登录用户，没有就返回null
		if (authentication != null) {
			if (authentication instanceof AnonymousAuthenticationToken) {
				return null;
			}
			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				return (User) authentication.getPrincipal();
			}
		}
		return null;
	}

}
