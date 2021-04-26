package com.yanfeitech.application.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.sound.midi.Soundbank;

import com.alibaba.druid.sql.visitor.functions.If;
import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.util.BeanUtil;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.enums.NoticeStatus;
import com.yanfeitech.application.enums.NoticeType;
import com.yanfeitech.application.vo.NoticeVO;

import net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.ForImplementation;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.impl.AddDelegateTransformer;
import org.springframework.security.web.server.authentication.AnonymousAuthenticationWebFilter;
import org.springframework.stereotype.Service;

import com.yanfeitech.application.dao.CourseDao;
import com.yanfeitech.application.dao.NoticeDao;
import com.yanfeitech.application.entity.Notice;
import com.yanfeitech.application.entity.SchoolClass;
import com.yanfeitech.application.common.page.PageResult;

/**
 * Notice的Service
 * 
 * @version 1.0.0
 * @since 2021-04-21 10:03:25
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class NoticeService {


	@Autowired
	private NoticeDao noticeDao;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private SchoolClassService schoolClassService;

	public Notice find(String id) {
		return noticeDao.get(id);
	}

	public List<Notice> findAll() {
		List<Notice> notices = noticeDao.selectAll();
		return notices;
	}

	public PageResult<Notice> findAllForPage(int pageNo, int pageSize) {
		PageResult<Notice> notices = new PageResult<>();
		notices = noticeDao.selectAllForPage(pageNo, pageSize);
		return notices;
	}

	public void save(Notice notice) {
		noticeDao.insert(notice);
	}

	public void save(List<Notice> notices) {
		noticeDao.insert(notices);
	}

	public void modify(List<Notice> notices) {
		noticeDao.update(notices);
	}

	public void delete(String id) {
		noticeDao.delete(id);
	}

	public ResultUtil pageByCondtion(PageParam<NoticeVO> pageParam) {
		if(pageParam.getCondition()==null) {
			NoticeVO noticeVO = new NoticeVO();
			pageParam.setCondition(noticeVO);
		}
		if (NoticeStatus.notValid(pageParam.getCondition().getStatus())) {
			return ResultUtil.fail("发布状态有误");
		}
		if (NoticeType.notValid(pageParam.getCondition().getType())){
			return ResultUtil.fail("消息类型有误");
		}
		PageResult<NoticeVO> noticeVOPageResult = noticeDao.selectKeyWord(pageParam);
		return ResultUtil.ok(noticeVOPageResult);
	}

	public ResultUtil pageByCondtions(PageParam<NoticeVO> pageParam) {
		// TODO Auto-generated method stub
		if(pageParam.getCondition()==null) {
			NoticeVO noticeVO = new NoticeVO();
			pageParam.setCondition(noticeVO);
		}
		if (NoticeStatus.notValid(pageParam.getCondition().getStatus())) {
			return ResultUtil.fail("发布状态有误");
		}
		if (NoticeType.notValid(pageParam.getCondition().getType())){
			return ResultUtil.fail("消息类型有误");
		}
		PageResult<NoticeVO> noticeVOPageResult = noticeDao.selectKeyWords(pageParam);
		return ResultUtil.ok(noticeVOPageResult);
		 
	}
	
	public ResultUtil pageByCondtionLikes(PageParam<NoticeVO> pageParam) {
		// TODO Auto-generated method stub
		PageResult<NoticeVO> noticeVOPageResult = noticeDao.selectLikeWords(pageParam);
		return ResultUtil.ok(noticeVOPageResult);
	}
	
	public ResultUtil saveNotices(NoticeVO noticevo) {
		// TODO Auto-generated method stub
		 ResultUtil judgment = judgment(noticevo);
		 if(ResultUtil.checkFail(judgment)) {
			 return judgment;
		 }
		 List<SchoolClass> findByIdList = schoolClassService.findByIdList(noticevo.getSchoolClassIdList());
		 List<SchoolClass> collect = findByIdList.stream().distinct().collect(Collectors.toList());
		 if (noticevo.getSchoolClassIdList().size()==collect.size()) {
			 Notice notice = new Notice();
			 notice.setType(noticevo.getType());
			 notice.setDetails(noticevo.getDetails());
			 notice.setSchoolClassId(noticevo.getSchoolClassIdList().toString().replaceAll("(?:\\[|null|\\]| +)", ""));
			 notice.setCourseId(noticevo.getCourseId());
			 noticeDao.insert(notice);
			 return ResultUtil.ok();
		}else {
			return	ResultUtil.fail("班级有误");
		}
	}
	public ResultUtil modify(NoticeVO noticevo) {
		Notice dbNotice = noticeDao.select(noticevo.getId());
		if (NoticeStatus.SENT.getId().equals(dbNotice.getStatus())) {
			return ResultUtil.fail("已发送的数据不能编辑");
		}
		ResultUtil judgment = judgment(noticevo);
		 if(ResultUtil.checkFail(judgment)) {
			 return judgment;
		 }
		 List<SchoolClass> findByIdList = schoolClassService.findByIdList(noticevo.getSchoolClassIdList());
		 List<SchoolClass> collect = findByIdList.stream().distinct().collect(Collectors.toList());
		 if (noticevo.getSchoolClassIdList().size()==collect.size()) {
			 noticevo.setStatus(dbNotice.getStatus());
		 BeanUtils.copyProperties(noticevo, dbNotice);
			 dbNotice.setSchoolClassId(noticevo.getSchoolClassIdList().toString().replaceAll("(?:\\[|null|\\]| +)", ""));
			 noticeDao.update(dbNotice);
			 return ResultUtil.ok();
		}else {
			return ResultUtil.fail("班级有误");
		}
	}
	public ResultUtil judgment(NoticeVO noticevo) {
		if(noticevo==null) {
			return ResultUtil.fail("参数有误");
		}
		if (StringUtils.isBlank(noticevo.getDetails())) {
			return ResultUtil.fail("内容不能为空");
		}
		if (StringUtils.isBlank(noticevo.getCourseId())){
			return ResultUtil.fail("课程不能为空");
		}
		if (CollectionUtils.isEmpty(noticevo.getSchoolClassIdList())) {
			return ResultUtil.fail("班级不能为空");
		}
		if (noticevo.getType()==null) {
			return ResultUtil.fail("类型不能为空");
		}
		if (courseService.find(noticevo.getCourseId()) == null) {
			return ResultUtil.fail("课程不存在");
		}
		return ResultUtil.ok();
	}

	public ResultUtil editType(Notice notice) {
		// TODO Auto-generated method stub
		if (notice.getId()==null) {
			ResultUtil.fail("id不存在");
		}
		noticeDao.update(notice);
		return ResultUtil.ok() ;
		
	}
}