package com.yanfeitech.application.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanfeitech.application.dao.StudentClassDao;
import com.yanfeitech.application.entity.StudentClass;
import com.yanfeitech.application.common.page.PageResult;

/**
 * StudentClass的Service
 * 
 * @version 1.0.0
 * @since 2021-04-25 10:58:22
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StudentClassService {

	@Autowired
	private StudentClassDao studentClassDao;

	public StudentClass find(String id) {
		return studentClassDao.get(id);
	}

	public List<StudentClass> findAll() {
		List<StudentClass> studentClasss = studentClassDao.selectAll();
		return studentClasss;
	}

	public PageResult<StudentClass> findAllForPage(int pageNo, int pageSize) {
		PageResult<StudentClass> studentClasss = new PageResult<>();
		studentClasss = studentClassDao.selectAllForPage(pageNo, pageSize);
		return studentClasss;
	}

	public void save(StudentClass studentClass) {
		studentClassDao.insert(studentClass);
	}

	public void save(List<StudentClass> studentClasss) {
		studentClassDao.insert(studentClasss);
	}

	public void modify(StudentClass studentClass) {
		studentClassDao.update(studentClass);
	}

	public void modify(List<StudentClass> studentClasss) {
		studentClassDao.update(studentClasss);
	}

	public void delete(String id) {
		studentClassDao.delete(id);
	}

}