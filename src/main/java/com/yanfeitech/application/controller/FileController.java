package com.yanfeitech.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yanfeitech.application.common.util.ResultUtil;
import com.yanfeitech.application.service.FileService;

/**
 * File的Controller
 * 
 * @version 1.0.0
 * @since 2020-11-24 12:52:10
 *
 */

@RequestMapping("/file")
@RestController
public class FileController {

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResultUtil upload(@RequestParam("file") MultipartFile file) {
		return fileService.upload(file);
	}

}