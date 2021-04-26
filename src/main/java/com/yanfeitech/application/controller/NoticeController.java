package com.yanfeitech.application.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Soundbank;

import com.yanfeitech.application.vo.NoticeVO;

import io.swagger.annotations.*;

import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yanfeitech.application.entity.Notice;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.service.NoticeService;
import com.yanfeitech.application.common.util.ResultUtil;

/**
 * Notice的Controller
 * 
 * @version 1.0.0
 * @since 2021-04-21 10:03:25
 *
 */

@RequestMapping("/notice")
@RestController
@Api(value="提供页面的增、删、改、查",description = "通知")
@ApiResponses({
		@ApiResponse(code = 200,message = "OK")
})
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ResultUtil<Notice> detail(String id) {
		Notice notice = noticeService.find(id);
		return ResultUtil.ok(notice);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResultUtil<List<Notice>> all() {
		List<Notice> notices = noticeService.findAll();
		return ResultUtil.ok(notices);
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST)
	public ResultUtil<PageResult<Notice>> page(@RequestBody PageParam<Notice> pageParam) {
		PageResult<Notice> notices = new PageResult<>();
		notices = noticeService.findAllForPage(pageParam.getPageNo(), pageParam.getPageSize());
		return ResultUtil.ok(notices);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ApiOperation(value="修改消息", notes="针对未发送消息进行课程的修改,班级的修改,通知类型的修改,详细内容的修改")
	@ApiParam(name="消息对象",value="传入json格式",required=true)
	public ResultUtil<NoticeVO> edit(@RequestBody @ApiParam(name="消息对象",value="传入json格式",required=true)NoticeVO noticevo) {
		return noticeService.modify(noticevo);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResultUtil<Notice> add(@RequestBody NoticeVO notice) {
		return noticeService.saveNotices(notice);
	}

	@RequestMapping(value = "/addBatch", method = RequestMethod.POST)
	public ResultUtil addbatch(@RequestBody List<Notice> notices) {
		noticeService.save(notices);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/editType", method = RequestMethod.POST)
	@ApiOperation(value="发送消息", notes="根据消息id修改状态为已发送")
	public ResultUtil editType(@RequestBody  Notice notice) {
		noticeService.editType(notice);
		return ResultUtil.ok();
	}
	
	@RequestMapping(value = "/editBatch", method = RequestMethod.POST)
	public ResultUtil editbatch(@RequestBody List<Notice> notices) {
		noticeService.modify(notices);
		return ResultUtil.ok();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultUtil delete(@RequestBody Notice notice) {
		noticeService.delete(notice.getId());
		return ResultUtil.ok();
	}

	@PostMapping("/pageByCondtion")
	public ResultUtil<PageParam<NoticeVO>> pageByCondtion(@RequestBody PageParam<NoticeVO> pageParam){
		//pageParam.getCondition().getSchoolClassId()
		return noticeService.pageByCondtion(pageParam);
	}
	
	
	@PostMapping("/pageByCondtions")
	public ResultUtil<PageParam<NoticeVO>> pageByCondtions(@RequestBody PageParam<NoticeVO> pageParam) {
		return noticeService.pageByCondtions(pageParam);
  }
	
	@PostMapping("/pageByCondtionLikes")
	@ApiOperation(value="根据条件查询消息", notes="根据班级id、发布状态、消息类型、以及关键字查询")
	public ResultUtil<PageParam<NoticeVO>> pageByCondtionLikes(@RequestBody PageParam<NoticeVO> pageParam) {
		return noticeService.pageByCondtionLikes(pageParam);
	  }

}