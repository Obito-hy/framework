package com.yanfeitech.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.entity.Courseware;
import com.yanfeitech.application.service.CoursewareService;

/**
 * Courseware的Controller
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:32
 *
 */

@RequestMapping("/courseware")
@RestController
public class CoursewareController {

	@Autowired
	private CoursewareService coursewareService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil detail(String id) {
		Courseware courseware = coursewareService.find(id);
		return ResultUtil.ok(courseware);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil all() {
		List<Courseware> coursewares = coursewareService.findAll();
		return ResultUtil.ok(coursewares);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil page(@RequestBody PageParam<Courseware> pageParam) {
		PageResult<Courseware> coursewares = new PageResult<>();
		coursewares = coursewareService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(coursewares);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil add(@RequestBody Courseware courseware) {
		coursewareService.save(courseware);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<Courseware> coursewares) {
		coursewareService.save(coursewares);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResultUtil edit(@RequestBody Courseware courseware) {
		coursewareService.modify(courseware);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<Courseware> coursewares) {
		coursewareService.modify(coursewares);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody Courseware courseware) {
		coursewareService.delete(courseware.getId());
		coursewareService.sortByNo(courseware.getChapterId());
		return ResultUtil.ok();
	}

	// 根据课时id查找课件列表
	@RequestMapping(value = "/findByChapterId", method = RequestMethod.GET)
	public ResultUtil findbychapterid(String chapterId) {
		List<Courseware> coursewares = coursewareService.findByChapterId(chapterId);
		return ResultUtil.ok(coursewares);
	}
}