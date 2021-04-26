package com.yanfeitech.application.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>
 * Title: PageResult
 * </p>
 * <p>
 * Description: 分页结果类，封装分页信息
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -4080861316463954445L;

	// 下一页
	private int nextPage;

	// 当前页
	private int currentPage;

	// 每页显示的条数
	private int pageSize;

	// 总页数
	private int pageCount;

	// 记录
	private List<T> results;

	// 记录总数
	private Integer total = 0;

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getNextPage() {
		if (nextPage <= 0) {
			return 1;
		} else {
			return nextPage > pageCount ? pageCount : nextPage;
		}
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> entities) {
		this.results = entities;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage <= 0 ? 1 : currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	public void resetNextPage() {
		nextPage = currentPage + 1;
		pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
