package com.yanfeitech.application.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.yanfeitech.application.common.util.ResultUtil;

/**
 * 
 * <p>
 * Title: GlobalExceptionHandler
 * </p>
 * <p>
 * Description: 自定义全局异常拦截，使错误信息不直接返回给用户
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResultUtil handlerException(Exception e) {
		e.printStackTrace();
		// 不暴露具体错误
		return ResultUtil.fail("服务器错误");
	}

	/**
	 * 处理权限校验不通过异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public ResultUtil validationError(AccessDeniedException ex) {
		return ResultUtil.fail("无权访问");
	}

	/**
	 * 处理实体字段校验不通过异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResultUtil validationError(MethodArgumentNotValidException ex) {
		return ResultUtil.fail(ex.getMessage());
	}

	/**
	 * 处理查不到数据异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	public ResultUtil validationError(EmptyResultDataAccessException ex) {
		ex.printStackTrace();
		return ResultUtil.fail("数据不存在");
	}

	/**
	 * 处理查不到数据异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(JpaObjectRetrievalFailureException.class)
	@ResponseBody
	public ResultUtil validationError(JpaObjectRetrievalFailureException ex) {
		ex.printStackTrace();
		return ResultUtil.fail("数据不存在");
	}

	/**
	 * 处理实体字段为空异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	@ResponseBody
	public ResultUtil validationError(InvalidDataAccessApiUsageException ex) {
		ex.printStackTrace();
		return ResultUtil.fail("数据无效，请检查是否输入id字段，或确认id对应数据是否存在");
	}

	/**
	 * 处理数据类型异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public ResultUtil validationError(MethodArgumentTypeMismatchException ex) {
		ex.printStackTrace();
		return ResultUtil.fail("数据类型输入错误");
	}

	/**
	 * 处理数据不完整异常
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	public ResultUtil validationError(DataIntegrityViolationException ex) {
		ex.printStackTrace();
		return ResultUtil.fail("请检查数据是否完整");
	}

	/**
	 * 处理请求方式错误
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public ResultUtil validationError(HttpRequestMethodNotSupportedException ex) {
		ex.printStackTrace();
		return ResultUtil.fail("请求方式错误");
	}
}
