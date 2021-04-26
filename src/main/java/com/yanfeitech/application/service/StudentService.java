package com.yanfeitech.application.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanfeitech.application.dao.StudentDao;
import com.yanfeitech.application.entity.Student;
import com.yanfeitech.application.common.page.PageResult;

/**
 * Student的Service
 * 
 * @version 1.0.0
 * @since 2021-04-25 10:58:22
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StudentService {

	@Autowired
	private StudentDao studentDao;

	public Student find(String id) {
		return studentDao.get(id);
	}

	public List<Student> findAll() {
		List<Student> students = studentDao.selectAll();
		return students;
	}

	public PageResult<Student> findAllForPage(int pageNo, int pageSize) {
		PageResult<Student> students = new PageResult<>();
		students = studentDao.selectAllForPage(pageNo, pageSize);
		return students;
	}

	public void save(Student student) {
		studentDao.insert(student);
	}

	public void save(List<Student> students) {
		studentDao.insert(students);
	}

	public void modify(Student student) {
		studentDao.update(student);
	}

	public void modify(List<Student> students) {
		studentDao.update(students);
	}

	public void delete(String id) {
		studentDao.delete(id);
	}

}