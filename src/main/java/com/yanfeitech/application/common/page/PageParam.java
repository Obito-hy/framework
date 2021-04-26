package com.yanfeitech.application.common.page;

import java.util.HashMap;

import lombok.*;

/**
 * 
 * <p>
 * Title: PageParam
 * </p>
 * <p>
 * Description: 分页参数（如果前端没有参数，那么默认查询第一页的10条数据）
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@Getter
@Setter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageParam<T> {

	// 查询第几页的数据
	private int pageNo = 1;
	// 每页显示多少条数据
	private int pageSize = 10;

	private T condition;

	private HashMap<String, Object> params;

}
