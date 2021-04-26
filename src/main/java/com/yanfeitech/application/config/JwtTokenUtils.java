package com.yanfeitech.application.config;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * <p>
 * Title: JwtTokenUtils
 * </p>
 * <p>
 * Description: jwt工具类，包含token配置以及操作方法
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class JwtTokenUtils {

	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";

	private static final String SECRET = "YANFEI_TECNOLOGY/*-+";
	private static final String ISS = "zhudelin";

	private static final long EXPIRATION = 7 * 24 * 60 * 60 * 1000;

	// 创建token
	public static String createToken(JwtUser jwtUser) {
		return Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET).setIssuer(ISS)
				.setSubject(JSONObject.toJSONString(jwtUser)).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)).compact();
	}

	// 从token中获取用户名
	public static JwtUser getUser(String token) {
		return JSONObject.parseObject(getTokenBody(token).getSubject(), JwtUser.class);
	}

	// 是否已过期
	public static boolean isExpiration(String token) {
		return getTokenBody(token).getExpiration().before(new Date());
	}

	private static Claims getTokenBody(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}
}