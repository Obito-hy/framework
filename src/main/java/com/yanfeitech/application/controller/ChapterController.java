package com.yanfeitech.application.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.entity.Chapter;
import com.yanfeitech.application.service.ChapterService;
import com.yanfeitech.application.vo.ChapterVO;

/**
 * Chapter的Controller
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:32
 *
 */

@RequestMapping("/chapter")
@RestController
public class ChapterController {

	@Autowired
	private ChapterService chapterService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil detail(String id) {
		Chapter chapter = chapterService.find(id);
		return ResultUtil.ok(chapter);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil all() {
		List<Chapter> chapters = chapterService.findAll();
		return ResultUtil.ok(chapters);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil page(@RequestBody PageParam<Chapter> pageParam) {
		PageResult<Chapter> chapters = new PageResult<>();
		chapters = chapterService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(chapters);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil add(@RequestBody Chapter chapter) {
		chapterService.save(chapter);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<Chapter> chapters) {
		chapterService.save(chapters);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResultUtil edit(@RequestBody Chapter chapter) {
		chapterService.modify(chapter);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<Chapter> chapters) {
		chapterService.modify(chapters);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody Chapter chapter) {
		if (CollectionUtils.isNotEmpty(chapterService.findChapterById(chapter.getId()).getCoursewareList())) {
			return ResultUtil.fail("该课时下存在课件，不可删除。");
		}
		chapterService.delete(chapter.getId());
		chapterService.sortByNo(chapter.getUnitId());
		return ResultUtil.ok();
	}

	// 根据id 查询课程的树形结构
	@RequestMapping(value = "/findByChapterId", method = RequestMethod.GET)
	public ResultUtil findByChapterId(String chapterId) {
		ChapterVO chpaterVO = new ChapterVO();
		chpaterVO = chapterService.findChapterById(chapterId);
		return ResultUtil.ok(chpaterVO);
	}

}