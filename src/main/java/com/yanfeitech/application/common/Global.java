package com.yanfeitech.application.common;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * <p>
 * Title: Global
 * </p>
 * <p>
 * Description: 全局变量类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Component
public class Global {

	public static String projectName = "framework";

	public static boolean isEnviormentWindows() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			return true;
		}
		return false;
	}

	public static String getBaseDir() {
		if (isEnviormentWindows()) {
			return "D:/" + projectName + "/";
		} else {
			return "/root/" + projectName + "/";
		}
	}

	public static String getDir(String dir) {
		String url = null;
		if (StringUtils.isNotBlank(dir)) {
			if (isEnviormentWindows()) {
				url = "D:/" + projectName + "/" + dir + "/";
			} else {
				url = "/root/" + projectName + "/" + dir + "/";
			}
		} else {
			return null;
		}
		File file = new File(url);
		if (!file.exists()) {
			file.mkdirs();
		}
		return url;
	}

	public static String getDir(String dir, String symbol) {
		String userDir = null;
		if (StringUtils.isNotBlank(dir)) {
			if (isEnviormentWindows()) {
				userDir = "D:/" + projectName + "/" + dir + "/" + symbol + "/";
			} else {
				userDir = "/root/" + projectName + "/" + dir + "/" + symbol + "/";
			}
		} else {
			return null;
		}
		File file = new File(userDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return userDir;
	}
}
