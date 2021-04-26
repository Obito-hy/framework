package com.yanfeitech.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.BeanUtil;
import com.yanfeitech.application.common.util.CommonUtil;
import com.yanfeitech.application.dao.ChapterDao;
import com.yanfeitech.application.entity.Chapter;
import com.yanfeitech.application.entity.Courseware;
import com.yanfeitech.application.entity.Resource;
import com.yanfeitech.application.vo.ChapterVO;
import com.yanfeitech.application.vo.CoursewareVO;

/**
 * Chapter的Service
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ChapterService {

	@Autowired
	private ChapterDao chapterDao;

	@Autowired
	private CoursewareService coursewareService;

	@Autowired
	private ResourceService resourceService;

	public Chapter find(String id) {
		return chapterDao.get(id);
	}

	public List<Chapter> findAll() {
		List<Chapter> chapters = chapterDao.selectAll();
		return chapters;
	}

	public PageResult<Chapter> findAllForPage(int pageNo, int pageSize) {
		PageResult<Chapter> chapters = new PageResult<>();
		chapters = chapterDao.selectAllForPage(pageNo, pageSize);
		return chapters;
	}

	public void save(Chapter chapter) {
		chapterDao.insert(chapter);
	}

	public void save(List<Chapter> chapters) {
		chapterDao.insert(chapters);
	}

	public void modify(Chapter chapter) {
		chapterDao.update(chapter);
	}

	public void modify(List<Chapter> chapters) {
		chapterDao.update(chapters);
	}

	public void delete(String id) {
		chapterDao.delete(id);
	}

//	public List<Chapter> findlistbyunitid(String unitId) {
//		List<Chapter> chapters = new ArrayList<Chapter>();
//		chapters = chapterDao.findlistbyunitid(unitId);
//		return chapters;
//	}

	public ChapterVO findvobychapterid(String chapterId) {
		ChapterVO chapterVO = new ChapterVO();
		Chapter chapter = new Chapter();
		chapter = chapterDao.get(chapterId);
		chapterVO.setChapterName(chapter.getChapterName());
		chapterVO.setChapterNo(chapter.getChapterNo());
		chapterVO.setId(chapter.getId());
		chapterVO.setUnitId(chapter.getUnitId());
		return chapterVO;
	}

	// 根据获取的章节列表中的id 获取课时列表
	public List<Chapter> findlistbyunitids(String unitIds) {
		List<Chapter> chapters = new ArrayList<Chapter>();
		chapters = chapterDao.findlistbyunitids(unitIds);
		return chapters;
	}

	public void sortByNo(String unitId) {
		List<Chapter> chapters = new ArrayList<>();
		chapters = chapterDao.findlistbyunitids(unitId);
		for (int i = 0; i < chapters.size(); i++) {
			Chapter chapter = chapters.get(i);
			String chapterName = "第" + CommonUtil.toChinese(chapter.getChapterNo()) + "课时";
			if (chapterName.equals(chapter.getChapterName())) {
				chapter.setChapterName("第" + CommonUtil.toChinese(i + 1) + "课时");
			}
			chapter.setChapterNo(i + 1);
		}
		modify(chapters);
	}

	// 根据课程id获取课程的树形结构
	public ChapterVO findChapterById(String chapterId) {
		Chapter chapterforvo = chapterDao.get(chapterId);
		ChapterVO chapterVO = new ChapterVO();
		List<Courseware> coursewares = new ArrayList<>();
		List<CoursewareVO> coursewareVOs = new ArrayList<>();
		coursewares = coursewareService.findByChapterId(chapterId);
		if (coursewares.size() == 0) {
			return BeanUtil.copyProperties(chapterforvo, ChapterVO.class);
		}
		String coursewareId;
		StringBuilder coursewareIds = new StringBuilder();
		for (int i = 0; i < coursewares.size(); i++) {
			coursewareId = coursewares.get(i).getId();
			coursewareIds.append(coursewareId).append("'").append(",").append("'");
		}
		coursewareIds.deleteCharAt(coursewareIds.length() - 1);
		coursewareIds.deleteCharAt(coursewareIds.length() - 1);
		coursewareIds.deleteCharAt(coursewareIds.length() - 1);
		List<Resource> resources = resourceService.findlistbycoursewareids(coursewareIds.toString());
		for (int i = 0; i < coursewares.size(); i++) {
			CoursewareVO coursewareVO = new CoursewareVO();
			coursewareVO = BeanUtil.copyProperties(coursewares.get(i), CoursewareVO.class);
			List<Resource> resourceVOs = new ArrayList<>();
			for (int j = 0; j < resources.size(); j++) {
				if (coursewares.get(i).getId().equals(resources.get(j).getCoursewareId())) {
					resourceVOs.add(resources.get(j));
					coursewareVO.setResourceList(resourceVOs);
				}
			}
			coursewareVOs.add(coursewareVO);
		}
		chapterVO.setChapterName(chapterforvo.getChapterName());
		chapterVO.setId(chapterforvo.getId());
		chapterVO.setCoursewareList(coursewareVOs);
		return chapterVO;
	}
}