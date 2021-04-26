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
import com.yanfeitech.application.dao.UnitDao;
import com.yanfeitech.application.entity.Unit;

/**
 * Unit的Service
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UnitService {

	@Autowired
	private UnitDao unitDao;

	public Unit find(String id) {
		return unitDao.get(id);
	}

	public List<Unit> findAll() {
		List<Unit> units = unitDao.selectAll();
		return units;
	}

	public PageResult<Unit> findAllForPage(int pageNo, int pageSize) {
		PageResult<Unit> units = new PageResult<>();
		units = unitDao.selectAllForPage(pageNo, pageSize);
		return units;
	}

	public void save(Unit unit) {
		unitDao.insert(unit);
	}

	public void save(List<Unit> units) {
		unitDao.insert(units);
	}

	public void modify(Unit unit) {
		unitDao.update(unit);
	}

	public void modify(List<Unit> units) {
		unitDao.update(units);
	}

	public void delete(String id) {
		unitDao.delete(id);
	}

	// 根据课程id 获取章节列表并排序
	public List<Unit> findListByCourseId(String courseId) {
		List<Unit> units = new ArrayList<>();
		units = unitDao.findlistbycourseid(courseId);
		return units;
	}

	public void sortByNo(String courseId) {
		List<Unit> units = new ArrayList<>();
		units = unitDao.findlistbycourseid(courseId);
		for (int i = 0; i < units.size(); i++) {
			Unit unit = units.get(i);
			String unitName = "第" + CommonUtil.toChinese(unit.getUnitNo()) + "章";
			if (unitName.equals(unit.getUnitName())) {
				unit.setUnitName("第" + CommonUtil.toChinese(i + 1) + "章");
			}
			unit.setUnitNo(i + 1);
		}
		modify(units);
	}

//	public UnitVO findvobyunitid(String unitId) {
//		Unit unit = unitDao.get(unitId);
//		UnitVO unitVO = new UnitVO();
//		ChapterVO chapterVO = new ChapterVO();
//		List<Chapter> chapters = new ArrayList<>();
//		chapters = chapterService.findlistbyunitid(unitId);
//		String chapterId;
//		List<ChapterVO> chapterVOs = new ArrayList<ChapterVO>();
//		for (int i = 0; i < chapters.size(); i++) {
//			chapterId = chapters.get(i).getId();
//			chapterVO = chapterService.findvobychapterid(chapterId);
//			chapterVOs.add(chapterVO);
//		}
//		unitVO.setCourseId(unit.getCourseId());
//		unitVO.setId(unit.getId());
//		unitVO.setUnitName(unit.getUnitName());
//		unitVO.setUnitNo(unit.getUnitNo());
//		unitVO.setChapterList(chapterVOs);
//		return unitVO;
//	}

}