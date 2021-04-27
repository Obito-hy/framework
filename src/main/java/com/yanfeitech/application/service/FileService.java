package com.yanfeitech.application.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.yanfeitech.application.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yanfeitech.application.common.Global;
import com.yanfeitech.application.common.util.CommonUtil;
import com.yanfeitech.application.common.util.LoginUserUtil;
import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.dao.FileDao;
import com.yanfeitech.application.entity.File;
import com.yanfeitech.application.entity.User;

/**
 * 
 * <p>
 * Title: FileService
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
public class FileService {
	@Autowired
	private FileDao fileDao;

	/**
	 * 
	 * <p>
	 * Title: upload
	 * </p>
	 * <p>
	 * Description: 上传文件
	 * </p>
	 * 
	 * @return ResultUtil
	 * @author zhudelin
	 * @date 2020年11月24日
	 * @version 1.0
	 */
	public ResultUtil upload(MultipartFile uploadFile) {
		if (uploadFile.isEmpty()) {
			return ResultUtil.fail("上传失败，请选择文件");
		}
		try {
			String originalName = uploadFile.getOriginalFilename();
			String fileType = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
			String fileName = CommonUtil.getRandomString(32) + fileType;
			User user = LoginUserUtil.getLoginUser();
			// 通过用户id区分文件夹
			String filePath = new StringBuilder().append(Global.getDir(user.getId())).append(fileName).toString();
			Path path = Paths.get(filePath);
			Files.write(path, uploadFile.getBytes());

			File file = new File();
			file.setRelativePath(filePath.replace(Global.getBaseDir(), "/"));
			file.setFileName(fileName);
			file.setFileSize(CommonUtil.convertFileSize(uploadFile.getSize()));
			file.setOriginalName(originalName);
			fileDao.insert(file);
			return ResultUtil.ok(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResultUtil.fail("上传失败");
	}



	public void save(File file) {
		fileDao.insert(file);
	}

	public void save(List<File> files) {
		fileDao.insert(files);
	}

	public void modify(File file) {
		fileDao.update(file);
	}

	public void modify(List<File> files) {
		fileDao.update(files);
	}
	public void deleteById(String id) {
		fileDao.deleteById(id);
	}
	public void delete(File file) {
		fileDao.delete(file);
	}
	public List<File> findId(String AssicuateId) {

		return fileDao.selectId(AssicuateId);
	}



}
