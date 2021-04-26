package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yanfeitech.application.vo.CourseVO;
import org.springframework.stereotype.Repository;

import com.yanfeitech.application.dao.config.BaseDao;

import com.yanfeitech.application.entity.Course;
import com.yanfeitech.application.vo.NoticeVO;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.Conditions;

/**
 * Course的DAO
 * 
  @version 1.0.0
 * @since 2021-04-21 10:03:25
 *
 */

@Repository
public class CourseDao extends BaseDao<Course, String> {

	public Course select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Course> selectAll() {
		return (ArrayList<Course>) getListBySql("select * from `course`",Course.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Course> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Course> courses = new PageResult<>();
		courses=(PageResult<Course>) findPageBySql("select * from `course`", pageNo, pageSize, Course.class, null);
		return courses;
	}

	public void insert(Course course) {
		save(course);
	}

	public void insert(List<Course> courses) {
		saveBatch(courses);
	}

	public void update(Course course) {
		super.update(course);
	}

	public void update(List<Course> courses) {
		updateBatch(courses);
	}

	public void delete(String id) {
		deleteById(id);
	}


	public List<CourseVO> selectStudentCourse(String studentId) {
		String sql = "SELECT st.id AS student_id, co.course_name, sc.classroom_number, sc.id AS school_calss_id FROM student st LEFT JOIN student_class sc ON st.id = sc.student_id LEFT JOIN course co ON sc.id = co.school_calss_id";
		Conditions conditions = new Conditions(sql);
		conditions.eq("student_id", studentId);
		List<CourseVO> listBySql = (List<CourseVO>) getListBySql(conditions.toString(),CourseVO.class);
		return listBySql;
	}

	public List<CourseVO> selectTeacherCourse(String teacherId) {
		String sql = "SELECT co.numbering AS course_numbering, co.course_name, sc.numbering AS classroomNumber, co.total_class_hours, co.lesson_preparation_ok, fi.assicuate_id, co.plan_description, co.outline_description FROM teacher te LEFT JOIN course co ON te.id = co.teacher_id LEFT JOIN school_class sc ON co.school_calss_id = sc.numbering LEFT JOIN file fi ON co.outline_description = fi.assicuate_id OR co.plan_description = fi.assicuate_id";
		Conditions conditions = new Conditions(sql);
		conditions.eq("teacher_id",teacherId);
		List<CourseVO> listBySql = (List<CourseVO>) getListBySql(conditions.toString(), CourseVO.class);
		return listBySql ;
	}
}