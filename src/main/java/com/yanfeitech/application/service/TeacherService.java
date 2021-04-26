package com.yanfeitech.application.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanfeitech.application.dao.TeacherDao;
import com.yanfeitech.application.entity.Teacher;
import com.yanfeitech.application.common.page.PageResult;

/**
 * Teacher的Service
 * 
 * @version 1.0.0
 * @since 2021-04-25 10:58:22
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class TeacherService {

	@Autowired
	private TeacherDao teacherDao;

	public Teacher find(String id) {
		return teacherDao.get(id);
	}

	public List<Teacher> findAll() {
		List<Teacher> teachers = teacherDao.selectAll();
		return teachers;
	}

	public PageResult<Teacher> findAllForPage(int pageNo, int pageSize) {
		PageResult<Teacher> teachers = new PageResult<>();
		teachers = teacherDao.selectAllForPage(pageNo, pageSize);
		return teachers;
	}

	public void save(Teacher teacher) {
		teacherDao.insert(teacher);
	}

	public void save(List<Teacher> teachers) {
		teacherDao.insert(teachers);
	}

	public void modify(Teacher teacher) {
		teacherDao.update(teacher);
	}

	public void modify(List<Teacher> teachers) {
		teacherDao.update(teachers);
	}

	public void delete(String id) {
		teacherDao.delete(id);
	}

}