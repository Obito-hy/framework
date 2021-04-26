package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.dao.config.BaseDao;

import com.yanfeitech.application.entity.Student;
import com.yanfeitech.application.common.page.PageResult;

/**
 * Student的DAO
 * 
  @version 1.0.0
 * @since 2021-04-25 10:58:22
 *
 */

@Repository
public class StudentDao extends BaseDao<Student, String> {

	public Student select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Student> selectAll() {
		return (ArrayList<Student>) getListBySql("select * from `student`",Student.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Student> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Student> students = new PageResult<>();
		students=(PageResult<Student>) findPageBySql("select * from `student`", pageNo, pageSize, Student.class, null);
		return students;
	}

	public void insert(Student student) {
		save(student);
	}

	public void insert(List<Student> students) {
		saveBatch(students);
	}

	public void update(Student student) {
		super.update(student);
	}

	public void update(List<Student> students) {
		updateBatch(students);
	}

	public void delete(String id) {
		deleteById(id);
	}

}