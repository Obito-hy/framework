package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.Conditions;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.User;

/**
 * 
 * <p>
 * Title: UserDao
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Repository
public class UserDao extends BaseDao<User, String> {

	public User select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<User> selectAll() {
		return (ArrayList<User>) getListBySql("select * from `user`", User.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<User> selectAllForPage(int pageNo, int pageSize) {
		PageResult<User> users = new PageResult<>();
		users = (PageResult<User>) findPageBySql("select * from `user`", pageNo, pageSize, User.class, null);
		return users;
	}

	public void insert(User user) {
		save(user);
	}

	public void insert(List<User> users) {
		saveBatch(users);
	}

	public void update(User user) {
		super.update(user);
	}

	public void update(List<User> users) {
		updateBatch(users);
	}

	public void delete(String id) {
		deleteById(id);
	}

	public User findByUsername(String username) {
		String sql = "select id,username,password,role from `user`";
		Conditions conditions = new Conditions(sql).eq("username", username);
		return (User) getBySql(conditions.toString(), new HashMap<>(), User.class);
	}
}