package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

import com.yanfeitech.application.common.util.Conditions;
import org.springframework.stereotype.Repository;

import com.yanfeitech.application.common.page.PageResult;
import com.yanfeitech.application.dao.config.BaseDao;
import com.yanfeitech.application.entity.File;

/**
 * 
 * <p>
 * Title: FileDao
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Repository
public class FileDao extends BaseDao<File, String> {

	public File select(String id) {
		return get(id);
	}

	@SuppressWarnings("unchecked")
	public List<File> selectAll() {
		return (ArrayList<File>) getListBySql("select * from `file`", File.class);
	}

	@SuppressWarnings("unchecked")
	public PageResult<File> selectAllForPage(int pageNo, int pageSize) {
		PageResult<File> files = new PageResult<>();
		files = (PageResult<File>) findPageBySql("select * from `file`", pageNo, pageSize, File.class, null);
		return files;
	}

	public void insert(File file) {
		save(file);
	}

	public void insert(List<File> files) {
		saveBatch(files);
	}

	public void update(File file) {
		super.update(file);
	}

	public void update(List<File> files) {
		updateBatch(files);
	}

	public void delete(File file) {
		delete(file);
	}
	public void delete(String id) {
		deleteById(id);
	}

	public List<File> selectId (String AssicuateId) {
//		Conditions conditions = new Conditions(sql)
//                .eq("sad.id", params.get("areaId"))
//                .or()
//                .append("sad.parent_area_id = " + params.get("areaId"))
//                .orderBy("order by sad.id");
		String sql ="SELECT id FROM file WHERE assicuate_name = \"course\" OR assicuate_key = \"plan_description\" OR assicuate_key = \"outline_description\" AND";
		Conditions conditions = new Conditions(sql);
		conditions.append("assicuate_id"+" "+"="+" "+"\""+AssicuateId+"\"");
		List<File> listBySql = (List<File>) getListBySql(conditions.toString(), File.class);
		return listBySql;
	}


}