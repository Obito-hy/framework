package com.yanfeitech.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.entity.DictItem;
import com.yanfeitech.application.service.DictItemService;

/**
 * DictItem的Controller
 * 
 * @version 1.0.0
 * @since 2020-11-24 12:52:10
 *
 */

@RequestMapping("/dict")
@RestController
public class DictItemController {

	@Autowired
	private DictItemService dictItemService;

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil detail(String id) {
		DictItem dictItem = dictItemService.find(id);
		return ResultUtil.ok(dictItem);
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil all() {
		List<DictItem> dictItems = dictItemService.findAll();
		return ResultUtil.ok(dictItems);
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil page(@RequestBody PageParam<DictItem> pageParam) {
		PageResult<DictItem> dictItems = new PageResult<>();
		dictItems = dictItemService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(dictItems);
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil add(@RequestBody DictItem dictItem) {
		dictItemService.save(dictItem);
		return ResultUtil.ok();
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<DictItem> dictItems) {
		dictItemService.save(dictItems);
		return ResultUtil.ok();
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResultUtil edit(@RequestBody DictItem dictItem) {
		dictItemService.modify(dictItem);
		return ResultUtil.ok();
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<DictItem> dictItems) {
		dictItemService.modify(dictItems);
		return ResultUtil.ok();
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody DictItem dictItem) {
		dictItemService.delete(dictItem.getId());
		return ResultUtil.ok();
	}

	@PreAuthorize("hasRole('SYSTEM_ADMIN')")
	@RequestMapping(value = "/refreshCache", method = RequestMethod.POST)
	public ResultUtil refreshCache() {
		return dictItemService.refreshCache();
	}

	@RequestMapping(value = "/findByType", method = RequestMethod.GET)
	public ResultUtil findByType(String type) {
		return dictItemService.findByType(type);
	}
}