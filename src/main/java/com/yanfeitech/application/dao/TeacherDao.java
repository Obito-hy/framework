package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.dao.config.BaseDao;

import com.yanfeitech.application.entity.Teacher;
import com.yanfeitech.application.common.page.PageResult;

/**
 * Teacher的DAO
 * 
  @version 1.0.0
 * @since 2021-04-25 10:58:22
 *
 */

@Repository
public class TeacherDao extends BaseDao<Teacher, String> {

	public Teacher select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> selectAll() {
		return (ArrayList<Teacher>) getListBySql("select * from `teacher`",Teacher.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Teacher> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Teacher> teachers = new PageResult<>();
		teachers=(PageResult<Teacher>) findPageBySql("select * from `teacher`", pageNo, pageSize, Teacher.class, null);
		return teachers;
	}

	public void insert(Teacher teacher) {
		save(teacher);
	}

	public void insert(List<Teacher> teachers) {
		saveBatch(teachers);
	}

	public void update(Teacher teacher) {
		super.update(teacher);
	}

	public void update(List<Teacher> teachers) {
		updateBatch(teachers);
	}

	public void delete(String id) {
		deleteById(id);
	}

}