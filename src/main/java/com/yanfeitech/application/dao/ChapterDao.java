package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.Chapter;

/**
 * Chapter的DAO
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Repository
public class ChapterDao extends BaseDao<Chapter, String> {

	public Chapter select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Chapter> selectAll() {
		return (ArrayList<Chapter>) getListBySql("select * from `chapter`", Chapter.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Chapter> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Chapter> chapters = new PageResult<>();
		chapters = (PageResult<Chapter>) findPageBySql("select * from `chapter`", pageNo, pageSize, Chapter.class,
				null);
		return chapters;
	}

	public void insert(Chapter chapter) {
		save(chapter);
	}

	public void insert(List<Chapter> chapters) {
		saveBatch(chapters);
	}

	public void update(Chapter chapter) {
		super.update(chapter);
	}

	public void update(List<Chapter> chapters) {
		updateBatch(chapters);
	}

	public void delete(String id) {
		deleteById(id);
	}

//	@SuppressWarnings("unchecked")
//	public List<Chapter> findlistbyunitid(String unitId) {
//		List<Chapter> chapters = new ArrayList<>();
//		chapters = (ArrayList<Chapter>) getListBySql(
//				"select ch.id,ch.chapter_name,ch.unit_id,ch.chapter_no from `chapter` ch where ch.unit_id = '" + unitId
//						+ "' order by ch.chapter_no asc ",
//				Chapter.class);
//		return chapters;
//	}

	// 根据获取的章节列表中的id 获取课时列表
	@SuppressWarnings("unchecked")
	public List<Chapter> findlistbyunitids(String unitIds) {
		List<Chapter> chapters = new ArrayList<>();
		chapters = (ArrayList<Chapter>) getListBySql(
				"select ch.id,ch.chapter_name,ch.unit_id,ch.chapter_no,ch.create_time,ch.update_time from `chapter` ch where ch.unit_id in ('"
						+ unitIds + "') order by ch.unit_id asc, ch.chapter_no asc ",
				Chapter.class);
		return chapters;
	}

}