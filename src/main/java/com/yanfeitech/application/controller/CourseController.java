package com.yanfeitech.application.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.entity.Course;
import com.yanfeitech.application.service.CourseService;
import com.yanfeitech.application.vo.CourseVO;

/**
 * Course的Controller
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:32
 *
 */

@RequestMapping("/course")
@RestController
public class CourseController {

	@Autowired
	private CourseService courseService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil detail(String id) {
		Course course = courseService.find(id);
		return ResultUtil.ok(course);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil all() {
		List<Course> courses = courseService.findAll();
		return ResultUtil.ok(courses);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil page(@RequestBody PageParam<Course> pageParam) {
		PageResult<Course> courses = new PageResult<>();
		courses = courseService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(courses);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil add(@RequestBody CourseVO courseVO) {

		return courseService.save(courseVO);
	}

	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<Course> courses) {
		courseService.save(courses);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResultUtil edit(@RequestBody CourseVO courseVO) {
		courseService.modify(courseVO);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<Course> courses) {
		courseService.modify(courses);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody Course course) {
//		if (CollectionUtils.isNotEmpty(courseService.findCourseByCourseId(course.getId()).getUnitList())) {
//			return ResultUtil.fail("该课时下存在目录，不可删除。");
//		}
		return courseService.delete(course);
	}

//	// 根据课程id 查询课程的树形结构
//	@RequestMapping(value = "/findByCourseId", method = RequestMethod.GET)
//	public ResultUtil findByCourseId(String courseId) {
//		CourseVO courseVO = new CourseVO();
//		courseVO = courseService.findCourseByCourseId(courseId);
//		return ResultUtil.ok(courseVO);
//	}

	@PostMapping(value = "/upLoadCourse")
	public ResultUtil upLoadCourse(@RequestBody CourseVO courseVO) {
		courseService.upLoadCourse(courseVO);
		return ResultUtil.ok();
	}

//	@PostMapping(value = "/studentByCourse")
//	public ResultUtil<List<CourseVO>> studentByCourse(@RequestBody String studentId) {
//		return courseService.studentByCourse(studentId);
//	}
//
//	@PostMapping(value = "/teacherByCourse")
//	public ResultUtil<List<CourseVO>> teacherByCourse(@RequestBody String teacherId) {
//		return courseService.teacherByCourse(teacherId);
//	}
	@PostMapping(value = "/roleByCourse")
	public ResultUtil<List<CourseVO>> roleByCourse() {
		return courseService.roleByCourse();
	}

}