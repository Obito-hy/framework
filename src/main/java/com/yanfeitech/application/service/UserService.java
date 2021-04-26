package com.yanfeitech.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.util.BeanUtil;
import com.yanfeitech.application.common.util.LoginUserUtil;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.dao.UserDao;
import com.yanfeitech.application.entity.User;
import com.yanfeitech.application.vo.UserVO;

/**
 * 
 * <p>
 * Title: UserService
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UserService {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserDao userDao;

	/**
	 * 
	 * <p>
	 * Title:用户注册
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param userVO
	 * @return
	 * @author zhudelin
	 * @date 2020年11月23日
	 * @version 1.0
	 */
	public ResultUtil register(UserVO userVO) {
		User user = BeanUtil.copyProperties(userVO, User.class);
		user.setId(null);
		user.setUsername(userVO.getUsername());
		// 注册的时候把密码加密一下
		user.setPassword(bCryptPasswordEncoder.encode(userVO.getPassword()));
		user.setRole("ROLE_USER");
		userDao.insert(user);
		return ResultUtil.ok();
	}

	/**
	 * 
	 * <p>
	 * Title:获取当前用户信息
	 * </p>
	 * <p>
	 * Description:用户密码不返回
	 * </p>
	 * 
	 * @return User
	 * @author zhudelin
	 * @date 2020年11月23日
	 * @version 1.0
	 */
	public ResultUtil info() {
		User user = LoginUserUtil.getLoginUser();
		user.setPassword(null);
		return ResultUtil.ok(user);
	}
}
