package com.yanfeitech.application.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanfeitech.application.dao.SchoolClassDao;
import com.yanfeitech.application.entity.SchoolClass;
import com.yanfeitech.application.common.page.PageResult;

/**
 * SchoolClass的Service
 * 
 * @version 1.0.0
 * @since 2021-04-21 10:03:25
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class SchoolClassService {

	@Autowired
	private SchoolClassDao schoolClassDao;

	public SchoolClass find(String id) {
		return schoolClassDao.get(id);
	}

	public List<SchoolClass> findAll() {
		List<SchoolClass> schoolClasss = schoolClassDao.selectAll();
		return schoolClasss;
	}

	public PageResult<SchoolClass> findAllForPage(int pageNo, int pageSize) {
		PageResult<SchoolClass> schoolClasss = new PageResult<>();
		schoolClasss = schoolClassDao.selectAllForPage(pageNo, pageSize);
		return schoolClasss;
	}

	public void save(SchoolClass schoolClass) {
		schoolClassDao.insert(schoolClass);
	}

	public void save(List<SchoolClass> schoolClasss) {
		schoolClassDao.insert(schoolClasss);
	}

	public void modify(SchoolClass schoolClass) {
		schoolClassDao.update(schoolClass);
	}

	public void modify(List<SchoolClass> schoolClasss) {
		schoolClassDao.update(schoolClasss);
	}

	public void delete(String id) {
		schoolClassDao.delete(id);
	}

	public List<SchoolClass> findByIdList(List<String> schoolClassIdList) {
		// TODO Auto-generated method stub
		 
		 return schoolClassDao.selectByIdList(schoolClassIdList);
	}



}