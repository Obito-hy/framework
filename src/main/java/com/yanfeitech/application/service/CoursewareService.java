package com.yanfeitech.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.CommonUtil;
import com.yanfeitech.application.dao.CoursewareDao;
import com.yanfeitech.application.entity.Courseware;

/**
 * Courseware的Service
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class CoursewareService {

	@Autowired
	private CoursewareDao coursewareDao;

	public Courseware find(String id) {
		return coursewareDao.get(id);
	}

	public List<Courseware> findAll() {
		List<Courseware> coursewares = coursewareDao.selectAll();
		return coursewares;
	}

	public PageResult<Courseware> findAllForPage(int pageNo, int pageSize) {
		PageResult<Courseware> coursewares = new PageResult<>();
		coursewares = coursewareDao.selectAllForPage(pageNo, pageSize);
		return coursewares;
	}

	public void save(Courseware courseware) {
		coursewareDao.insert(courseware);
	}

	public void save(List<Courseware> coursewares) {
		coursewareDao.insert(coursewares);
	}

	public void modify(Courseware courseware) {
		coursewareDao.update(courseware);
	}

	public void modify(List<Courseware> coursewares) {
		coursewareDao.update(coursewares);
	}

	public void delete(String id) {
		coursewareDao.delete(id);
	}

	public void sortByNo(String chapterId) {
		List<Courseware> coursewares = new ArrayList<>();
		coursewares = coursewareDao.findByChapterId(chapterId);
		for (int i = 0; i < coursewares.size(); i++) {
			Courseware courseware = coursewares.get(i);
			String coursewareName = "第" + CommonUtil.toChinese(courseware.getCoursewareNo()) + "页";
			if (coursewareName.equals(courseware.getTitle())) {
				courseware.setTitle("第" + CommonUtil.toChinese(i + 1) + "页");
			}
			courseware.setCoursewareNo(i + 1);
		}
		modify(coursewares);
	}

	// 根据课时id查找课件列表
	public List<Courseware> findByChapterId(String chapterId) {
		List<Courseware> coursewares = coursewareDao.findByChapterId(chapterId);
		return coursewares;
	}
}