package com.yanfeitech.application.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.util.BeanUtil;
import com.yanfeitech.application.config.JwtUser;
import com.yanfeitech.application.dao.UserDao;
import com.yanfeitech.application.entity.User;

/**
 * 
 * <p>
 * Title: UserDetailsServiceImpl
 * </p>
 * <p>
 * Description: 该Service用于springsecurity检验登录的用户名密码
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		JwtUser jwtUser = null;
		try {
			jwtUser = BeanUtil.copyProperties(user, JwtUser.class);
		} catch (Exception e) {
			return null;
		}
		return jwtUser;
	}

}