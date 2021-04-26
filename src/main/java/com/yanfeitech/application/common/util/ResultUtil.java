package com.yanfeitech.application.common.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>
 * Title: ResultUtil
 * </p>
 * <p>
 * Description: 返回简单封装
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultUtil<T> {

	// 响应业务状态
	private Integer status;

	// 响应消息
	private String message;

	// 响应中的数据
	private T data;

	public static <T> ResultUtil<T> ok(T data) {
		return new ResultUtil(200, "成功", data);
	}

	public static <T> ResultUtil<T> ok() {
		return ok(null);
	}

	public static <T> ResultUtil<T> fail() {
		return fail("失败");
	}

	public static <T> ResultUtil<T> fail(String message) {
		return new ResultUtil(100, message, null);
	}
	
	public static Boolean checkFail(ResultUtil result) {
		return ResultUtil.fail().getStatus().equals(result.getStatus());
	}
	
	public static Boolean checkSuccess(ResultUtil result) {
		return ResultUtil.ok().getStatus().equals(result.getStatus());
	}
}
