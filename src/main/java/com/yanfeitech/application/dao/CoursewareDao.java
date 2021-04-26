package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.Courseware;

/**
 * Courseware的DAO
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Repository
public class CoursewareDao extends BaseDao<Courseware, String> {

	public Courseware select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Courseware> selectAll() {
		return (ArrayList<Courseware>) getListBySql("select * from `courseware`", Courseware.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Courseware> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Courseware> coursewares = new PageResult<>();
		coursewares = (PageResult<Courseware>) findPageBySql("select * from `courseware`", pageNo, pageSize,
				Courseware.class, null);
		return coursewares;
	}

	public void insert(Courseware courseware) {
		save(courseware);
	}

	public void insert(List<Courseware> coursewares) {
		saveBatch(coursewares);
	}

	public void update(Courseware courseware) {
		super.update(courseware);
	}

	public void update(List<Courseware> coursewares) {
		updateBatch(coursewares);
	}

	public void delete(String id) {
		deleteById(id);
	}

	// 根据课时id查找课件列表
	@SuppressWarnings({ "unchecked" })
	public List<Courseware> findByChapterId(String chapterId) {
		List<Courseware> coursewares = new ArrayList<>();
		coursewares = (ArrayList<Courseware>) getListBySql(
				"select cw.id,cw.chapter_id,cw.title,cw.type,cw.courseware_no from `courseware` cw where cw.chapter_id = '"
						+ chapterId + "' order by cw.courseware_no asc",
				Courseware.class);
		return coursewares;
	}
}