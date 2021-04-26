package com.yanfeitech.application.dao;

import java.util.ArrayList;
import java.util.List;

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

	public void delete(String id) {
		deleteById(id);
	}

}