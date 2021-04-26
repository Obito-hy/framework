package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.dao.config.BaseDao;

import com.yanfeitech.application.entity.SchoolClass;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.Conditions;

/**
 * SchoolClass的DAO
 * 
  @version 1.0.0
 * @since 2021-04-21 10:03:25
 *
 */

@Repository
public class SchoolClassDao extends BaseDao<SchoolClass, String> {

	public SchoolClass select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<SchoolClass> selectAll() {
		return (ArrayList<SchoolClass>) getListBySql("select * from `school_class`",SchoolClass.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<SchoolClass> selectAllForPage(int pageNo, int pageSize) {
		PageResult<SchoolClass> schoolClasss = new PageResult<>();
		schoolClasss=(PageResult<SchoolClass>) findPageBySql("select * from `school_class`", pageNo, pageSize, SchoolClass.class, null);
		return schoolClasss;
	}

	public void insert(SchoolClass schoolClass) {
		save(schoolClass);
	}

	public void insert(List<SchoolClass> schoolClasss) {
		saveBatch(schoolClasss);
	}

	public void update(SchoolClass schoolClass) {
		super.update(schoolClass);
	}

	public void update(List<SchoolClass> schoolClasss) {
		updateBatch(schoolClasss);
	}

	public void delete(String id) {
		deleteById(id);
	}

	public  List<SchoolClass> selectByIdList(List<String> schoolClassIdList) {
		// TODO Auto-generated method stub
		String sql = "SELECT id FROM school_class";
		Conditions conditions = new Conditions(sql)
				.in("id",schoolClassIdList.toArray());
		List<SchoolClass> listBySql = (List<SchoolClass>) getListBySql(conditions.toString(), SchoolClass.class);
		return listBySql;
	}

	

}