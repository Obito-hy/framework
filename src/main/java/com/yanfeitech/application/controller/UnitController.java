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
import com.yanfeitech.application.entity.Unit;
import com.yanfeitech.application.service.UnitService;

/**
 * Unit的Controller
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:32
 *
 */

@RequestMapping("/unit")
@RestController
public class UnitController {

	@Autowired
	private UnitService unitService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil detail(String id) {
		Unit unit = unitService.find(id);
		return ResultUtil.ok(unit);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil all() {
		List<Unit> units = unitService.findAll();
		return ResultUtil.ok(units);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil page(@RequestBody PageParam<Unit> pageParam) {
		PageResult<Unit> units = new PageResult<>();
		units = unitService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(units);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil add(@RequestBody Unit unit) {
		unitService.save(unit);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<Unit> units) {
		unitService.save(units);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResultUtil edit(@RequestBody Unit unit) {
		unitService.modify(unit);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<Unit> units) {
		unitService.modify(units);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody Unit unit) {
		unitService.delete(unit.getId());
		unitService.sortByNo(unit.getCourseId());
		return ResultUtil.ok();
	}

}