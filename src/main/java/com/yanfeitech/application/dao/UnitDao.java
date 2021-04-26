package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.Unit;

/**
 * Unit的DAO
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Repository
public class UnitDao extends BaseDao<Unit, String> {

	public Unit select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Unit> selectAll() {
		return (ArrayList<Unit>) getListBySql("select * from `unit`", Unit.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Unit> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Unit> units = new PageResult<>();
		units = (PageResult<Unit>) findPageBySql("select * from `unit`", pageNo, pageSize, Unit.class, null);
		return units;
	}

	public void insert(Unit unit) {
		save(unit);
	}

	public void insert(List<Unit> units) {
		saveBatch(units);
	}

	public void update(Unit unit) {
		super.update(unit);
	}

	public void update(List<Unit> units) {
		updateBatch(units);
	}

	public void delete(String id) {
		deleteById(id);
	}

	// 根据课程id 获取章节列表并排序
	@SuppressWarnings("unchecked")
	public List<Unit> findlistbycourseid(String courseId) {
		List<Unit> units = new ArrayList<>();
		units = (ArrayList<Unit>) getListBySql(
				"select un.id,un.course_id,un.unit_name,un.unit_no from `unit` un where un.course_id='" + courseId
						+ "' order by un.unit_no asc",
				Unit.class);
		return units;
	}

}