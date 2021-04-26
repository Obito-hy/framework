package com.yanfeitech.application.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * 
 * <p>
 * Title: JWTAuthorizationFilter
 * </p>
 * <p>
 * Description: jwt检验权限的拦截器
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private final List<String> releaseUrlList = Arrays.asList(SecurityConfig.RELEASE_URLS);

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		for (String url : releaseUrlList) {
			if (matching(url, request.getRequestURI())) {
				chain.doFilter(request, response);
				return;
			}
		}
		String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
		SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
		super.doFilterInternal(request, response, chain);
	}

	// 这里从token中获取用户信息并新建一个token
	private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
		if (StringUtils.isBlank(tokenHeader)) {
			return null;
		}
		String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
		JwtUser jwtUser = JwtTokenUtils.getUser(token);
		if (jwtUser != null) {
			return new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
		}
		return null;
	}

	// url路径匹配
	private boolean matching(String reg, String input) {
		if ("/*".equals(reg))
			return true;
		// 按 * 切割字符串
		String[] reg_split = reg.split("\\*");
		int index = 0, reg_len = reg_split.length;
		// b代表匹配模式的最后一个字符是否是 '*' ,因为在split方法下最后一个 * 会被舍弃
		boolean b = reg.charAt(reg.length() - 1) == '*' ? true : false;
		while (input.length() > 0) {
			// 如果匹配到最后一段,比如这里reg的landingsuper
			if (index == reg_len) {
				if (b)// 如果reg最后一位是 * ,代表通配,后面的就不用管了,返回true
					return true;
				else // 后面没有通配符 * ,但是input长度还没有变成0 (此时input = context=%7B%22nid%22%3...),显然不匹配
					return false;
			}
			String r = reg_split[index++];
			int indexOf = input.indexOf(r);
			if (indexOf == -1)
				return false;
			// 前面匹配成功的就可以不用管了,截取掉直接从新地方开始
			input = input.substring(indexOf + r.length());
		}
		return true;
	}
}