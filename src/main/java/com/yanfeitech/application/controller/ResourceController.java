package com.yanfeitech.application.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.entity.Resource;
import com.yanfeitech.application.service.ResourceService;

/**
 * Resource的Controller
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:32
 *
 */

@RequestMapping("/resource")
@RestController
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil detail(String id) {
		Resource resource = resourceService.find(id);
		return ResultUtil.ok(resource);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil all() {
		List<Resource> resources = resourceService.findAll();
		return ResultUtil.ok(resources);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil page(@RequestBody PageParam<Resource> pageParam) {
		PageResult<Resource> resources = new PageResult<>();
		resources = resourceService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(resources);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil add(@RequestBody Resource resource) {
		resourceService.save(resource);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<Resource> resources) {
		resourceService.save(resources);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResultUtil edit(@RequestBody Resource resource) {
		resourceService.modify(resource);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<Resource> resources) {
		resourceService.modify(resources);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody Resource resource) {
		resourceService.delete(resource.getId());
		resourceService.sortByNo(resource.getCoursewareId());
		return ResultUtil.ok();
	}

	// 根据课件id查找资源列表
	@RequestMapping(value = "/findbycoursewareid", method = RequestMethod.POST)
	public ResultUtil findbycoursewareid(@RequestBody PageParam<Resource> pageParam) {
		PageResult<Resource> resources = new PageResult<>();
		resources = resourceService.findbycoursewareid(pageParam);
		return ResultUtil.ok(resources);
	}

	// 课件id查资源
	@RequestMapping(value = "/findlistbycoursewareid", method = RequestMethod.GET)
	public ResultUtil findlistbycoursewareid(String coursewareId) {
		List<Resource> resources = new ArrayList<Resource>();
		resources = resourceService.findListByCoursewareId(coursewareId);
		return ResultUtil.ok(resources);
	}
}