package com.yanfeitech.application.service;

import java.util.ArrayList;
import java.util.List;

import com.yanfeitech.application.common.util.LoginUserUtil;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.entity.*;
import com.yanfeitech.application.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.BeanUtil;
import com.yanfeitech.application.dao.CourseDao;

/**
 * Course的Service
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class CourseService {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private FileService fileService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private ChapterService chapterService;

	public Course find(String id) {
		return courseDao.get(id);
	}

	public List<Course> findAll() {
		List<Course> courses = courseDao.selectAll();
		return courses;
	}

	public PageResult<Course> findAllForPage(int pageNo, int pageSize) {
		PageResult<Course> courses = new PageResult<>();
		courses = courseDao.selectAllForPage(pageNo, pageSize);
		return courses;
	}

	public ResultUtil save(CourseVO courseVO) {
		if (judgment(courseVO).getStatus()!=200) {
			return ResultUtil.fail("参数有误");
		}
		upLoadCourse(courseVO);
		return ResultUtil.ok();
	}
	public ResultUtil judgment(CourseVO courseVO) {
		if(courseVO==null) {
			return ResultUtil.fail("参数有误");
		}
		if (StringUtils.isBlank(courseVO.getCourseName())) {
			return ResultUtil.fail("课程名不能为空");
		}
		return ResultUtil.ok();
	}

	public void save(List<Course> courses) {
		courseDao.insert(courses);
	}

	public ResultUtil modify(CourseVO courseVO) {
		Course course = new Course();
		BeanUtils.copyProperties(courseVO,course);
		List<File> id = fileService.findId(course.getId());
		for (File file : id) {
			fileService.deleteById(file.getId());
		}
		List<File> outLineFileList = getFileList(course.getId(), courseVO.getOutlineFileList(), "outlineDescription");
		fileService.save(outLineFileList);
		List<File> planFileList = getFileList(course.getId(), courseVO.getPlanFileList(), "planFileList");
		fileService.save(planFileList);
		ResultUtil saveResult = modify(courseVO);
		return saveResult;
	}

	public void modify(List<Course> courses) {
		courseDao.update(courses);
	}

	public ResultUtil delete(Course course) {
		String id = LoginUserUtil.getLoginUser().getId();
		String teacherId = course.getTeacherId();
		if (!StringUtils.equals(id,teacherId)){
			return ResultUtil.fail("您没有权限");
		}
		Course select = courseDao.select(course.getId());
		String teacherIds = select.getTeacherId();
		String teacherIdes = course.getTeacherId();
		if (!StringUtils.equals(teacherIds,teacherIdes)){
			return ResultUtil.fail("您没有权限");
		}
		courseDao.delete(course.getId());
		return  ResultUtil.ok() ;
	}

	// 根据课程id获取课程的树形结构
	public CourseVO findCourseByCourseId(String courseId) {
		Course courseforvo = courseDao.get(courseId);
		CourseVO courseVO = new CourseVO();
		List<Unit> units = new ArrayList<>();
		List<UnitVO> unitVOs = new ArrayList<>();
		units = unitService.findListByCourseId(courseId);
		if (units.size() == 0) {
			return BeanUtil.copyProperties(courseforvo, CourseVO.class);
		}
		List<Chapter> chapters = new ArrayList<>();
		String unitId;
		StringBuilder unitIds = new StringBuilder();
		for (int i = 0; i < units.size(); i++) {
			unitId = units.get(i).getId();
			unitIds.append(unitId).append("'").append(",").append("'");
		}
		unitIds.deleteCharAt(unitIds.length() - 1);
		unitIds.deleteCharAt(unitIds.length() - 1);
		unitIds.deleteCharAt(unitIds.length() - 1);
		chapters = chapterService.findlistbyunitids(unitIds.toString());
		for (int i = 0; i < units.size(); i++) {
			UnitVO unitVO = new UnitVO();
			unitVO.setCourseId(units.get(i).getCourseId());
			unitVO.setId(units.get(i).getId());
			unitVO.setUnitName(units.get(i).getUnitName());
			unitVO.setUnitNo(units.get(i).getUnitNo());
			List<ChapterVO> chapterVOs = new ArrayList<>();
			for (int j = 0; j < chapters.size(); j++) {
				if (units.get(i).getId().equals(chapters.get(j).getUnitId())) {
					ChapterVO chapterVO = new ChapterVO();
					chapterVO.setChapterName(chapters.get(j).getChapterName());
					chapterVO.setChapterNo(chapters.get(j).getChapterNo());
					chapterVO.setId(chapters.get(j).getId());
					chapterVO.setUnitId(chapters.get(j).getUnitId());
					chapterVOs.add(chapterVO);
					unitVO.setChapterList(chapterVOs);
				}
			}
			unitVOs.add(unitVO);
		}
		courseVO.setCourseName(courseforvo.getCourseName());
		courseVO.setId(courseforvo.getId());
		courseVO.setTextbookVersion(courseforvo.getTextbookVersion());
		courseVO.setUnitList(unitVOs);
		return courseVO;
	}

	public ResultUtil<CourseVO> upLoadCourse(CourseVO courseVO) {
		Course course=new Course();
		String role = LoginUserUtil.getLoginUser().getRole();
		if ("ROLE_TEACHER".equals(role)){
			return ResultUtil.fail("无权限");
		}
		String roleId = LoginUserUtil.getLoginUser().getId();
		course.setTeacherId(roleId);
		BeanUtils.copyProperties(courseVO,course);
		courseDao.save(course);
		List<File> outLineFileList = getFileList(course.getId(), courseVO.getOutlineFileList(), "outlineDescription");
		fileService.save(outLineFileList);
		List<File> planFileList = getFileList(course.getId(), courseVO.getPlanFileList(), "planFileList");
		fileService.save(planFileList);
		return ResultUtil.ok();
	}

	public List<File> getFileList(String courseId,List<FileVO> fileVOList,String key){
		List<File> outLineFileList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(fileVOList)) {
			outLineFileList = BeanUtil.copyList(fileVOList, File.class);
			for (File file : outLineFileList) {
				file.setAssicuateName("course");
				file.setAssicuateId(courseId);
				file.setAssicuateKey(key);
			}
		}
		return outLineFileList;
	}

	public ResultUtil<List<CourseVO>> roleByCourse() {
		User user =LoginUserUtil.getLoginUser();
		String role = user.getRole();
		List<CourseVO> courseVOS=new ArrayList<>();
		if ("ROLE_TEACHER".equals(role)){
			courseVOS =courseDao.selectTeacherCourse(user.getId());
		}else {
			 courseVOS = courseDao.selectStudentCourse(user.getId());
		}
		return ResultUtil.ok(courseVOS);
	}
}