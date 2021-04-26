package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.vo.NoticeVO;
import org.springframework.stereotype.Repository;

import com.yanfeitech.application.dao.config.BaseDao;

import com.yanfeitech.application.entity.Notice;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.Conditions;

/**
 * Notice的DAO
 * 
 * @version 1.0.0
 * @since 2021-04-21 10:03:25
 *
 */

@Repository
public class NoticeDao extends BaseDao<Notice, String> {

	public Notice select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Notice> selectAll() {
		return (ArrayList<Notice>) getListBySql("select * from `notice`", Notice.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Notice> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Notice> notices = new PageResult<>();
		notices = (PageResult<Notice>) findPageBySql("select * from `notice`", pageNo, pageSize, Notice.class, null);
		return notices;
	}

	public void insert(Notice notice) {
		save(notice);
	}

	public void insert(List<Notice> notices) {
		saveBatch(notices);
	}

	public void update(Notice notice) {
		super.update(notice);
	}

	public void update(List<Notice> notices) {
		updateBatch(notices);
	}

	public void delete(String id) {
		deleteById(id);
	}

	public PageResult<NoticeVO> selectKeyWord(PageParam<NoticeVO> pageParam) {
		int pageNo = pageParam.getPageNo();
		int pageSize = pageParam.getPageSize();
		NoticeVO condition = pageParam.getCondition();
		String sql = "select n.type,n.status ,n.details,n.school_class_id,n.course_id,sc.numbering AS classNumbering,co.numbering AS courseNumbering from notice n Left Join school_class sc ON sc.numbering = n.school_class_id Left Join course co ON co.numbering = n.course_id";
		Conditions conditions = new Conditions(sql)
				.eq("n.type", condition.getType())
				.eq("n.status", condition.getStatus())
				.eq("n.school_class_id", condition.getSchoolClassId())
				.eq("n.course_id", condition.getCourseId())
				.like("n.details", condition.getDetails());
		PageResult<NoticeVO> pageBySql = (PageResult<NoticeVO>) findPageBySql(conditions.toString(),pageNo, pageSize, NoticeVO.class, null);
		return pageBySql;
	}

	public PageResult<NoticeVO> selectKeyWords(PageParam<NoticeVO> pageParam) {
		// TODO Auto-generated method stub
		int pageNo = pageParam.getPageNo();
		int pageSize = pageParam.getPageSize();
		NoticeVO condition = pageParam.getCondition();
		String sql = "select n.type,n.status ,n.school_class_id,n.course_id,sc.numbering AS classNumbering,co.numbering AS courseNumbering from notice n Left Join school_class sc ON sc.numbering = n.school_class_id Left Join course co ON co.numbering = n.course_id";
		Conditions conditions = new Conditions(sql)
				.eq("n.type", condition.getType())
				.eq("n.status", condition.getStatus())
				.in("n.school_class_id", condition.getSchoolClassIdList().toArray())
				.eq("n.course_id", condition.getCourseId());
		PageResult<NoticeVO> pageBySql = (PageResult<NoticeVO>) findPageBySql(conditions.toString(),pageNo, pageSize, NoticeVO.class, null);
		return pageBySql;
	}

	public PageResult<NoticeVO> selectLikeWords(PageParam<NoticeVO> pageParam) {
		// TODO Auto-generated method stub
		int pageNo = pageParam.getPageNo();
		int pageSize = pageParam.getPageSize();
		NoticeVO condition = pageParam.getCondition();
		String sql = "select n.type,n.status ,n.school_class_id,n.course_id,sc.numbering AS classNumbering,co.numbering AS courseNumbering from notice n Left Join school_class sc ON sc.numbering = n.school_class_id Left Join course co ON co.numbering = n.course_id";
		Conditions conditions = new Conditions(sql)
				.eq("n.type", condition.getType())
				.eq("n.status", condition.getStatus())
				.like("n.school_class_id", condition.getSchoolClassId())
				.eq("n.course_id", condition.getCourseId());
		PageResult<NoticeVO> pageBySql = (PageResult<NoticeVO>) findPageBySql(conditions.toString(),pageNo, pageSize, NoticeVO.class, null);
		return pageBySql;
	}
}