package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageParam;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.Resource;

/**
 * Resource的DAO
 * 
 * @version 1.0.0
 * @since 2021-02-05 10:23:31
 *
 */

@Repository
public class ResourceDao extends BaseDao<Resource, String> {

	public Resource select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<Resource> selectAll() {
		return (ArrayList<Resource>) getListBySql("select * from `resource`", Resource.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<Resource> selectAllForPage(int pageNo, int pageSize) {
		PageResult<Resource> resources = new PageResult<>();
		resources = (PageResult<Resource>) findPageBySql("select * from `resource`", pageNo, pageSize, Resource.class,
				null);
		return resources;
	}

	public void insert(Resource resource) {
		save(resource);
	}

	public void insert(List<Resource> resources) {
		saveBatch(resources);
	}

	public void update(Resource resource) {
		super.update(resource);
	}

	public void update(List<Resource> resources) {
		updateBatch(resources);
	}

	public void delete(String id) {
		deleteById(id);
	}

	// 根据课件id查找资源列表
	@SuppressWarnings("unchecked")
	public PageResult<Resource> findbycoursewareid(PageParam<Resource> pageParam) {
		PageResult<Resource> resources = new PageResult<>();
		resources = (PageResult<Resource>) findPageBySql(
				"select re.id,re.action_point,re.action_steps,re.content_description,re.content_detail,re.courseware_id,re.file_url,re.graph_type,re.graph_else_type,re.resource_name,re.resource_no,re.type from `resource` re where re.courseware_id = '"
						+ pageParam.getCondition().getCoursewareId() + "' order by re.resource_no asc",
				pageParam.getPageNo(), pageParam.getPageSize(), Resource.class, null);
		return resources;
	}

	// 根据课件ID查询课件资源列表，返回，
	@SuppressWarnings("unchecked")
	public List<Resource> findListByCoursewareId(String coursewareId) {
		List<Resource> resources = new ArrayList<Resource>();
		resources = (ArrayList<Resource>) getListBySql(
				"select re.id,re.courseware_id,re.action_point,re.action_steps,re.content_description,re.content_detail,re.file_url,re.graph_type,re.graph_else_type,re.resource_name,re.resource_no,re.type from `resource` re where re.courseware_id = '"
						+ coursewareId + "' order by re.resource_no asc ",
				Resource.class);
		return resources;
	}

	@SuppressWarnings("unchecked")
	public List<Resource> findlistbycoursewareids(String coursewareIds) {
		List<Resource> resources = new ArrayList<Resource>();
		resources = (ArrayList<Resource>) getListBySql(
				"select re.id,re.courseware_id,re.action_point,re.action_steps,re.content_description,re.content_detail,re.file_url,re.graph_type,re.graph_else_type,re.resource_name,re.resource_no,re.type from `resource` re where re.courseware_id in ('"
						+ coursewareIds + "') order by re.resource_no asc ",
				Resource.class);
		return resources;
	}

}