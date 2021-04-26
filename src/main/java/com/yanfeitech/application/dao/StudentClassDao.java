package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.dao.config.BaseDao;

import com.yanfeitech.application.entity.StudentClass;
import com.yanfeitech.application.common.page.PageResult;

/**
 * StudentClass的DAO
 * 
  @version 1.0.0
 * @since 2021-04-25 10:58:22
 *
 */

@Repository
public class StudentClassDao extends BaseDao<StudentClass, String> {

	public StudentClass select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<StudentClass> selectAll() {
		return (ArrayList<StudentClass>) getListBySql("select * from `student_class`",StudentClass.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<StudentClass> selectAllForPage(int pageNo, int pageSize) {
		PageResult<StudentClass> studentClasss = new PageResult<>();
		studentClasss=(PageResult<StudentClass>) findPageBySql("select * from `student_class`", pageNo, pageSize, StudentClass.class, null);
		return studentClasss;
	}

	public void insert(StudentClass studentClass) {
		save(studentClass);
	}

	public void insert(List<StudentClass> studentClasss) {
		saveBatch(studentClasss);
	}

	public void update(StudentClass studentClass) {
		super.update(studentClass);
	}

	public void update(List<StudentClass> studentClasss) {
		updateBatch(studentClasss);
	}

	public void delete(String id) {
		deleteById(id);
	}

}