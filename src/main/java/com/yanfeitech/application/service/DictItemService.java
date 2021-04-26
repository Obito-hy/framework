package com.yanfeitech.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanfeitech.application.common.constant.RedisKeysConstant;
import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.common.util.CommonUtil;
import com.yanfeitech.application.common.util.RedisUtil;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.dao.DictItemDao;
import com.yanfeitech.application.entity.DictItem;
import com.yanfeitech.application.vo.DictVO;

/**
 * 
 * <p>
 * Title: DictItemService
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class DictItemService {

	@Autowired
	private DictItemDao dictItemDao;

	public DictItem find(String id) {
		return dictItemDao.get(id);
	}

	public List<DictItem> findAll() {
		List<DictItem> dictItems = dictItemDao.selectAll();
		return dictItems;
	}

	public PageResult<DictItem> findAllForPage(int pageNo, int pageSize) {
		PageResult<DictItem> dictItems = new PageResult<>();
		dictItems = dictItemDao.selectAllForPage(pageNo, pageSize);
		return dictItems;
	}

	public void save(DictItem dictItem) {
		dictItemDao.insert(dictItem);
	}

	public void save(List<DictItem> dictItems) {
		dictItemDao.insert(dictItems);
	}

	public void modify(DictItem dictItem) {
		dictItemDao.update(dictItem);
	}

	public void modify(List<DictItem> dictItems) {
		dictItemDao.update(dictItems);
	}

	public void delete(String id) {
		dictItemDao.delete(id);
	}

	/**
	 * 
	 * <p>
	 * Title: findByType
	 * </p>
	 * <p>
	 * Description: 根据类型查找字典
	 * </p>
	 * 
	 * @return ResultUtil<dictVOs>
	 * @author zhudelin
	 * @date 2020年11月24日
	 * @version 1.0
	 */
	public ResultUtil findByType(String type) {
		if (StringUtils.isBlank(type)) {
			return ResultUtil.fail("类型不能为空");
		}
		List<DictVO> dictVOs = new ArrayList<>();
		Map<Object, Object> map = RedisUtil.getHash(RedisKeysConstant.BASE_DICT_ITEM_KEY + type);
		if (map.isEmpty()) {
			dictVOs = dictItemDao.selectByType(type);
			if (!dictVOs.isEmpty()) {
				RedisUtil.putHash(RedisKeysConstant.BASE_DICT_ITEM_KEY + type, CommonUtil.ObjectCovertToMap(dictVOs));
			}
		} else {
			for (Map.Entry<Object, Object> object : map.entrySet()) {
				DictVO dictVO = new DictVO();
				dictVO.setCode((String) object.getKey());
				dictVO.setName((String) object.getValue());
				dictVOs.add(dictVO);
			}
		}
		return ResultUtil.ok(dictVOs);
	}

	/**
	 * 
	 * <p>
	 * Title: refreshCache
	 * </p>
	 * <p>
	 * Description: 刷新redis缓存，从数据库从新载入
	 * </p>
	 * 
	 * @return ResultUtil
	 * @author zhudelin
	 * @date 2020年11月24日
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public ResultUtil refreshCache() {
		List<DictItem> dictItems = dictItemDao.selectAllByOrder();
		List<String> redisTypes = (List<String>) RedisUtil.get(RedisKeysConstant.BASE_DICT_ITEM_KEY_ALLTYPE);
		if (CollectionUtils.isNotEmpty(redisTypes)) {
			for (String type : redisTypes) {
				Map<Object, Object> map = RedisUtil.getHash(RedisKeysConstant.BASE_DICT_ITEM_KEY + type);
				List<Object> keys = new ArrayList<>();
				map.forEach((key, value) -> {
					keys.add(key);
				});
				if (!keys.isEmpty()) {
					RedisUtil.deleteHashValue(RedisKeysConstant.BASE_DICT_ITEM_KEY + type,
							keys.toArray(new Object[keys.size()]));
				}
			}
			if (RedisUtil.hasKey(RedisKeysConstant.BASE_DICT_ITEM_KEY_ALLTYPE)) {
				RedisUtil.del(RedisKeysConstant.BASE_DICT_ITEM_KEY_ALLTYPE);
			}
		}
		if (CollectionUtils.isNotEmpty(dictItems)) {
			List<String> allItemType = new ArrayList<>();
			for (DictItem item : dictItems) {
				RedisUtil.putHashValue(RedisKeysConstant.BASE_DICT_ITEM_KEY + item.getType(), item.getCode(),
						item.getName());
				if (!allItemType.contains(item.getType())) {
					allItemType.add(item.getType());
				}
				RedisUtil.put(RedisKeysConstant.BASE_DICT_ITEM_KEY_ALLTYPE, allItemType);
			}
			return ResultUtil.ok();
		}
		return ResultUtil.fail("字典表空");
	}

}
