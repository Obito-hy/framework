package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.Conditions;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.DictItem;
import com.yanfeitech.application.vo.DictVO;

/**
 * 
 * <p>
 * Title: DictItemDao
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Repository
public class DictItemDao extends BaseDao<DictItem, String> {

	public DictItem select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<DictItem> selectAll() {
		return (ArrayList<DictItem>) getListBySql("select * from `dict_item`", DictItem.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<DictItem> selectAllForPage(int pageNo, int pageSize) {
		PageResult<DictItem> dictItems = new PageResult<>();
		dictItems = (PageResult<DictItem>) findPageBySql("select * from `dict_item`", pageNo, pageSize, DictItem.class,
				null);
		return dictItems;
	}

	public void insert(DictItem dictItem) {
		save(dictItem);
	}

	public void insert(List<DictItem> dictItems) {
		saveBatch(dictItems);
	}

	public void update(DictItem dictItem) {
		super.update(dictItem);
	}

	public void update(List<DictItem> dictItems) {
		updateBatch(dictItems);
	}

	public void delete(String id) {
		deleteById(id);
	}

	@SuppressWarnings({ "all" })
	public List<DictVO> selectByType(String type) {
		String sql = "select code,name from dict_item";
		Conditions conditions = new Conditions(sql).eq("type", type)
				.orderBy("order by order_no ASC,code ASC,create_time DESC");
		return getListBySql(conditions.toString(), new HashMap(), DictVO.class);
	}

	@SuppressWarnings({ "all" })
	public List<DictItem> selectAllByOrder() {
		String sql = "select code,name,type from dict_item";
		Conditions conditions = new Conditions(sql).orderBy("order by type ASC,order_no ASC,code ASC,create_time DESC");
		return getListBySql(conditions.toString(), new HashMap(), DictItem.class);
	}

}