package com.yanfeitech.application.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yanfeitech.application.common.util.ResultUtil;

/**
 * 
 * <p>
 * Title: JWTAuthenticationFilter
 * </p>
 * <p>
 * Description: jwt解析检验token的拦截器
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		super.setFilterProcessesUrl("/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		BufferedReader br;
		String requestBody = "";
		try {
			br = request.getReader();
			String str;
			while ((str = br.readLine()) != null) {
				requestBody += str;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JSONObject body = JSON.parseObject(requestBody);
		try {
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					body.getString("username"), body.getString("password"), new ArrayList<>()));
		} catch (Exception e) {
			try {
				unsuccessfulAuthentication(request, response, null);
			} catch (IOException | ServletException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	// 成功验证后调用的方法
	// 如果验证成功，就生成token并返回
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		// 查看源代码会发现调用getPrincipal()方法会返回一个实现了`UserDetails`接口的对象
		// 所以就是JwtUser啦
		JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
		String token = JwtTokenUtils.createToken(jwtUser);
		// 返回创建成功的token
		// 但是这里创建的token只是单纯的token
		// 按照jwt的规定，最后请求的格式应该是 `Bearer token`
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(JSONObject.toJSONString(ResultUtil.ok(JwtTokenUtils.TOKEN_PREFIX + token)));
	}

	// 这是验证失败时候调用的方法
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(JSONObject.toJSONString(ResultUtil.fail("用户名/密码错误")));
	}
}