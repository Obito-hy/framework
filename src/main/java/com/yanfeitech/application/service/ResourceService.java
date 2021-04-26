package com.yanfeitech.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.CommonUtil;
import com.yanfeitech.application.dao.ResourceDao;
import com.yanfeitech.application.entity.Resource;

/**
 * Resource的Service
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ResourceService {

	@Autowired
	private ResourceDao resourceDao;

	public Resource find(String id) {
		return resourceDao.get(id);
	}

	public List<Resource> findAll() {
		List<Resource> resources = resourceDao.selectAll();
		return resources;
	}

	public PageResult<Resource> findAllForPage(int pageNo, int pageSize) {
		PageResult<Resource> resources = new PageResult<>();
		resources = resourceDao.selectAllForPage(pageNo, pageSize);
		return resources;
	}

	public void save(Resource resource) {
		resourceDao.insert(resource);
	}

	public void save(List<Resource> resources) {
		resourceDao.insert(resources);
	}

	public void modify(Resource resource) {
		resourceDao.update(resource);
	}

	public void modify(List<Resource> resources) {
		resourceDao.update(resources);
	}

	public void delete(String id) {
		resourceDao.delete(id);
	}

	// 根据课件id查找资源列表
	public PageResult<Resource> findbycoursewareid(PageParam<Resource> pageParam) {
		PageResult<Resource> resources = new PageResult<>();
		resources = resourceDao.findbycoursewareid(pageParam);
		return resources;
	}

	public List<Resource> findListByCoursewareId(String coursewareId) {
		List<Resource> resources = new ArrayList<>();
		resources = resourceDao.findListByCoursewareId(coursewareId);
		return resources;
	}

	// 根据获取的章节列表中的id 获取课时列表
	public List<Resource> findlistbycoursewareids(String coursewareIds) {
		return resourceDao.findlistbycoursewareids(coursewareIds);
	}

	public void sortByNo(String unitId) {
		List<Resource> resources = new ArrayList<>();
		resources = resourceDao.findlistbycoursewareids(unitId);
		for (int i = 0; i < resources.size(); i++) {
			Resource resource = resources.get(i);
			String resourceName = "第" + CommonUtil.toChinese(resource.getResourceNo()) + "条内容";
			if (resourceName.equals(resource.getResourceName())) {
				resource.setResourceName("第" + CommonUtil.toChinese(i + 1) + "条内容");
			}
			resource.setResourceNo(i + 1);
		}
		modify(resources);
	}
}