package com.yanfeitech.application.common.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * 
 * <p>
 * Title: Conditions
 * </p>
 * <p>
 * Description: SQL条件动态查询工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class Conditions {

	/**
	 * 单引号（类型为字符串时使用）
	 */
	private static final String SINGLE_QUOTE = "'";
	/**
	 * 逗号
	 */
	private static final String COMMA = ",";
	/**
	 * 空格
	 */
	private static final String SPACE = " ";
	/**
	 * 大于
	 */
	private static final String GTR = ">";
	/**
	 * 小于
	 */
	private static final String LEQ = "<";
	/**
	 * WHERE
	 */
	private static final String OPERATION_WHERE = "WHERE";
	/**
	 * BETWEEN
	 */
	private static final String OPERATION_BETWEEN = "BETWEEN";
	/**
	 * AND
	 */
	public static final String OPERATION_AND = "AND";
	/**
	 * OR
	 */
	public static final String OPERATION_OR = "OR";
	/**
	 * LIKE
	 */
	public static final String OPERATION_LIKE = "LIKE";
	/**
	 * IN
	 */
	public static final String OPERATION_IN = "IN";
	/**
	 * NOT
	 */
	public static final String OPERATION_NOT = "NOT";
	/**
	 * 左括号
	 */
	public static final String OPERATION_BRANKET_LEFT = "(";
	/**
	 * 右括号
	 */
	public static final String OPERATION_BRANKET_RIGHT = ")";
	/**
	 * =（等号）
	 */
	public static final String OPERATION_EQUALS = "=";
	/**
	 * %（百分号，LIKE使用）
	 */
	public static final String OPERATION_PERCENT = "%";
	/**
	 * IS
	 */
	private static final Object OPERATION_IS = "IS";
	/**
	 * order by
	 */
	private static final String ORDERBY = "ORDER BY";
	/**
	 * 大于等于
	 */
	private static final String GLT_EQUALS = ">=";
	/**
	 * 小于等于
	 */
	private static final String LEQ_EQUALS = "<=";
	/**
	 * having
	 */
	private static final String HAVING = "HAVING";
	/**
	 * 动态SQL语句
	 */
	private StringBuilder sql;

	/**
	 * SQL语句中是否已经有了WHERE（避免在SQL语句中使用 1=1）
	 */
	private boolean hasWhere = false;

	/**
	 * SQL语句中是否已经有了HAVING（避免在SQL语句中使用 1=1）
	 */
	private boolean hasHaving = false;

	public Conditions() {
		this.sql = new StringBuilder();
	}

	public Conditions(String sql) {
		this.sql = new StringBuilder(sql);
	}

	public Conditions(String sql, boolean hasWhere) {
		this.sql = new StringBuilder(sql).append(SPACE);
		this.hasWhere = hasWhere;
	}

	public Conditions(StringBuilder sql, boolean hasWhere) {
		this.sql = sql.append(SPACE);
		this.hasWhere = hasWhere;
	}

	/**
	 * WHERE条件中 相等 的方法
	 *
	 * @param columnName 列名（根据SQL的具体情况，可能需要这样的形式：表别名.列名）
	 * @param value      值
	 * @return Conditions
	 */
	public Conditions eq(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_EQUALS).append(SPACE);
		// 字符串需要单引号
		if (value instanceof String) {
			if (StringUtils.isBlank((String) value)) {
				return this;
			}
			value = new StringBuilder().append(SINGLE_QUOTE).append(value.toString()).append(SINGLE_QUOTE);
		}
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(value).append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(value).append(SPACE));
		}
		return this;
	}

	/**
	 * @param columnName
	 * @param value
	 * @return
	 * @author 佘伦
	 * @Desciription in的拼接
	 */
	public Conditions in(String columnName, Object[] value) {
		if (null == value || value.length == 0) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_IN).append(SPACE)
				.append(OPERATION_BRANKET_LEFT);
		for (int i = 0; i < value.length; i++) {
			// 字符串需要单引号
			if (value[i] instanceof String) {
				if (StringUtils.isBlank((String) value[i])) {
					return this;
				}
				if (i != value.length - 1) {
					sb.append(SINGLE_QUOTE).append(value[i].toString()).append(SINGLE_QUOTE).append(COMMA);
				} else {
					sb.append(SINGLE_QUOTE).append(value[i].toString()).append(SINGLE_QUOTE);
				}
			} else {
				if (i != value.length - 1) {
					sb.append(value[i]).append(COMMA);
				} else {
					sb.append(value[i]);
				}
			}
		}
		sb.append(OPERATION_BRANKET_RIGHT);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	public Conditions inOr(String columnName, Object[] value) {
		if (null == value || value.length == 0) {
			return this;
		}
		int length = value.length;
		StringBuilder sb = new StringBuilder();

		double flag = Math.ceil(Double.valueOf(length) / 1000D);
		int result1 = (int) flag;
		if (result1 == 1) {
			addIn(columnName, value, sb, 0, length);
		} else {
			addIn(columnName, value, sb, 0, 1000);
		}
		for (int i = 1; i < result1; i++) {
			if (i != result1 - 1) {
				sb.append(SPACE).append(OPERATION_OR).append(SPACE);
				addIn(columnName, value, sb, i * 1000, (i + 1) * 1000);
			} else {
				sb.append(SPACE).append(OPERATION_OR).append(SPACE);
				addIn(columnName, value, sb, i * 1000, length);
			}
		}

		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(OPERATION_BRANKET_LEFT)
					.append(sb.append(SPACE)).append(OPERATION_BRANKET_RIGHT).append(SPACE);
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(OPERATION_BRANKET_LEFT).append(SPACE)
					.append(sb.append(OPERATION_BRANKET_RIGHT).append(SPACE));
		}
		return this;
	}

	public void addIn(String columnName, Object[] value, StringBuilder sb, int start, int length) {
		sb.append(columnName).append(SPACE).append(OPERATION_IN).append(SPACE).append(OPERATION_BRANKET_LEFT);
		for (int i = start; i < length; i++) {
			// 字符串需要单引号
			if (value[i] instanceof String) {
				if (StringUtils.isBlank((String) value[i])) {
					continue;
				}
				if (i != length - 1) {
					sb.append(SINGLE_QUOTE).append(value[i].toString()).append(SINGLE_QUOTE).append(COMMA);
				} else {
					sb.append(SINGLE_QUOTE).append(value[i].toString()).append(SINGLE_QUOTE);
				}
			} else {
				if (i != length - 1) {
					sb.append(value[i]).append(COMMA);
				} else {
					sb.append(value[i]);
				}
			}
		}
		sb.append(OPERATION_BRANKET_RIGHT);
	}

	/**
	 * @param columnName
	 * @param value
	 * @return
	 * @author 佘伦
	 * @Desciription not in的拼接
	 */
	public Conditions notIn(String columnName, Object[] value) {
		if (null == value || value.length == 0) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_NOT).append(SPACE)
				.append(OPERATION_IN).append(SPACE).append(OPERATION_BRANKET_LEFT);
		for (int i = 0; i < value.length; i++) {
			// 字符串需要单引号
			if (value[i] instanceof String) {
				if (StringUtils.isBlank((String) value[i])) {
					return this;
				}
				if (i != value.length - 1) {
					sb.append(SINGLE_QUOTE).append(value[i].toString()).append(SINGLE_QUOTE).append(COMMA);
				} else {
					sb.append(SINGLE_QUOTE).append(value[i].toString()).append(SINGLE_QUOTE);
				}
			} else {
				if (i != value.length - 1) {
					sb.append(value[i]).append(COMMA);
				} else {
					sb.append(value[i]);
				}
			}
		}
		sb.append(OPERATION_BRANKET_RIGHT);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	/**
	 * @param columnName
	 * @param value      null 或者 not null
	 * @return
	 * @author 佘伦
	 * @Desciription is null 或者 is not null的拼接
	 */
	public Conditions is(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		String newValue = (String) value;
		if (StringUtils.isBlank(newValue)) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_IS).append(SPACE)
				.append(newValue);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	/**
	 * WHERE条件中 LIKE（模糊查询） 的方法
	 *
	 * @param columnName 列名（根据SQL的具体情况，可能需要这样的形式：表别名.列名）
	 * @param value      值
	 * @return Conditions
	 */
	public Conditions like(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		String newValue = value.toString();
		if (StringUtils.isBlank(newValue)) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_LIKE).append(SPACE);
		newValue = new StringBuilder().append(SINGLE_QUOTE).append(OPERATION_PERCENT).append(newValue)
				.append(OPERATION_PERCENT).append(SINGLE_QUOTE).toString();
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(newValue).append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(newValue).append(SPACE));
		}
		return this;
	}

	// WHERE columnName LIKE 'value%'
	public Conditions likes(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		String newValue = value.toString();
		if (StringUtils.isBlank(newValue)) {
			return this;
		}

		// columnName LIKE
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_LIKE).append(SPACE);
		// 'value%'
		newValue = new StringBuilder().append(SINGLE_QUOTE).append(newValue).append(OPERATION_PERCENT)
				.append(SINGLE_QUOTE).toString();
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(newValue).append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(newValue).append(SPACE));
		}
		return this;
	}

	// WHERE (columnName LIKE 'value1%' OR columnName LIKE 'value2%')
	public Conditions likeList(String columnName, List<String> values) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < values.size(); i++) {
			String valueString = values.get(i);
			if (StringUtils.isBlank(valueString)) {
				return this;
			}
			stringBuffer.append(SPACE).append(columnName).append(SPACE).append(OPERATION_LIKE).append(SPACE)
					.append(SINGLE_QUOTE).append(valueString).append(OPERATION_PERCENT).append(SINGLE_QUOTE);
			// 如果不是最后一个参数，后面都要拼接一个 or
			if (i != values.size() - 1) {
				stringBuffer.append(SPACE).append(OPERATION_OR);
			}
		}
		// 完毕之后前后拼接 括号
		String sql = OPERATION_BRANKET_LEFT + stringBuffer.toString() + OPERATION_BRANKET_RIGHT;
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sql);
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sql);
		}
		return this;
	}

	/**
	 * 拼接groupBy语句
	 *
	 * @param groupBy 待拼接的groupBy语句
	 * @return Conditions
	 */
	public Conditions groupBy(String groupBy) {
		this.sql.append(SPACE).append(groupBy).append(SPACE);
		return this;
	}

	/**
	 * 拼接having语句
	 *
	 * @param value 值
	 * @return Conditions
	 */
	public Conditions having(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_EQUALS).append(SPACE);
		// 字符串需要单引号
		if (value instanceof String) {
			if (StringUtils.isBlank((String) value)) {
				return this;
			}
			value = new StringBuilder().append(SINGLE_QUOTE).append(value.toString()).append(SINGLE_QUOTE);
		}
		// HAVING判断
		if (!hasHaving) {
			this.hasHaving = true;
			this.sql.append(SPACE).append(HAVING).append(SPACE).append(sb.append(value).append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(value).append(SPACE));
		}
		return this;
	}

	/**
	 * 拼接orderBy语句
	 *
	 * @param orderBy 待拼接的orderBy语句
	 * @return Conditions
	 */
	public Conditions orderBy(String orderBy) {
		this.sql.append(SPACE).append(orderBy).append(SPACE);
		return this;
	}

	/**
	 * 拼接orderBy语句
	 *
	 * @param columnName 待排序的字段
	 * @param ascOrDesc  正排序或者倒排序
	 * @return Conditions
	 */
	public Conditions orderBy(Object columnName, Object ascOrDesc) {
		if (null == columnName || null == ascOrDesc) {
			return this;
		}
		String columnValue = columnName.toString();
		String ascOrDescValue = ascOrDesc.toString();
		if (StringUtils.isBlank(columnValue) || StringUtils.isBlank(ascOrDescValue)) {
			return this;
		}
		this.sql.append(SPACE).append(ORDERBY).append(SPACE).append(columnName).append(SPACE).append(ascOrDesc);
		return this;
	}

	/**
	 * 拼接between..and..语句
	 *
	 * @param startValue 值
	 * @param endValue   值
	 * @return Conditions
	 */
	public Conditions between(String columnName, Object startValue, Object endValue) {
		if (null == startValue || null == endValue) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(OPERATION_BETWEEN).append(SPACE)
				.append(startValue).append(SPACE).append(OPERATION_AND).append(SPACE).append(endValue);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	/**
	 * 拼接gtr语句
	 *
	 * @param value 值
	 * @return Conditions
	 */
	public Conditions gtr(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(GTR).append(SPACE).append(value);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	/**
	 * 拼接leq语句
	 *
	 * @param value 值
	 * @return Conditions
	 */
	public Conditions leq(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(LEQ).append(SPACE).append(value);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	public Conditions append(Conditions after) {
		String afterSql = after.sql.toString();
		if (StringUtils.isBlank(afterSql)
				|| StringUtils.isBlank(afterSql.replace(OPERATION_AND, SPACE).replace(OPERATION_OR, SPACE)
						.replace(OPERATION_BRANKET_LEFT, SPACE).replace(OPERATION_BRANKET_RIGHT, SPACE))) {
			return this;
		}
		this.sql.append(SPACE).append(afterSql);
		return this;
	}

	public Conditions append(String after) {
		this.sql.append(SPACE).append(after);
		return this;
	}

	public Conditions append(Object value, String after) {
		this.sql.append(SPACE).append(after);
		return this;
	}

	public Conditions between(Object date, String betweenAnd) {
		if (null == date) {
			return this;
		}
		String toDate = "to_date('" + new DateTime(date).toString("yyyy-MM-dd") + "', 'yyyy-mm-dd'" + ")";
		this.sql.append(toDate).append(SPACE).append(betweenAnd).append(SPACE);
		return this;
	}

	public Conditions between(Object date, String beginDate, String endDate) {
		if (null == date) {
			return this;
		}
		String toDate = "to_date('" + new DateTime(date).toString("yyyy-MM-dd") + "', 'yyyy-mm-dd'" + ")";
		this.sql.append(toDate).append(SPACE).append(OPERATION_BETWEEN).append(SPACE).append(beginDate).append(SPACE)
				.append(OPERATION_AND).append(SPACE).append(endDate).append(SPACE);
		return this;
	}

	public Conditions between(String date, String beginDate, String endDate) {
		if (null == date) {
			return this;
		}
		if (StringUtils.isBlank(date) || StringUtils.isBlank(beginDate) || StringUtils.isBlank(endDate)) {
			return this;
		}
		String toDate = "to_char(" + date + ", 'yyyy-mm-dd')";
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(toDate).append(SPACE)
					.append(OPERATION_BETWEEN).append(SPACE).append(SINGLE_QUOTE).append(beginDate).append(SINGLE_QUOTE)
					.append(SPACE).append(OPERATION_AND).append(SPACE).append(SINGLE_QUOTE).append(endDate)
					.append(SINGLE_QUOTE).append(SPACE);
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(toDate).append(SPACE)
					.append(OPERATION_BETWEEN).append(SPACE).append(SINGLE_QUOTE).append(beginDate).append(SINGLE_QUOTE)
					.append(SPACE).append(OPERATION_AND).append(SPACE).append(SINGLE_QUOTE).append(endDate)
					.append(SINGLE_QUOTE).append(SPACE);
		}
		return this;
	}

	public Conditions between(String date, Object begin, Object end, Object nothing) {
		if (null == begin) {
			return this;
		}
		String between = "between to_date('" + new DateTime(begin).toString("yyyy-MM-dd") + "', 'yyyy-mm-dd'" + ")";
		String and = "and to_date('" + new DateTime(end).toString("yyyy-MM-dd") + "', 'yyyy-mm-dd'" + ")";
		this.sql.append(date).append(SPACE).append(between).append(SPACE).append(and).append(SPACE);
		return this;
	}

	/**
	 * @param columnName
	 * @param startTime
	 * @param endTime
	 * @param nothing
	 * @return
	 * @author 佘伦
	 * @Desciription 增加了字符串从开始到结束时间的区间查询(包含了结束日期当天)
	 */
	public Conditions between(String columnName, String startTime, String endTime, Object nothing) {
		if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
			return this;
		}
		String between = "between to_date('" + startTime + "', 'yyyy-mm-dd'" + ")";
		String and = "and (to_date('" + endTime + "', 'yyyy-mm-dd'" + ")+1)";
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(columnName).append(SPACE)
					.append(between).append(SPACE).append(and).append(SPACE);
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(columnName).append(SPACE).append(between)
					.append(SPACE).append(and).append(SPACE);
		}
		return this;
	}

	public Conditions and() {
		this.sql.append(OPERATION_AND).append(SPACE);
		return this;
	}

	public Conditions or() {
		this.sql.append(OPERATION_OR).append(SPACE);
		return this;
	}

	public Conditions branketLeft() {
		this.sql.append(OPERATION_BRANKET_LEFT).append(SPACE);
		return this;
	}

	public Conditions branketRight() {
		this.sql.append(OPERATION_BRANKET_RIGHT).append(SPACE);
		return this;
	}

	/**
	 * 拼接gtrEquals语句
	 *
	 * @param value 值
	 * @return Conditions
	 */
	public Conditions gtrEquals(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(GLT_EQUALS).append(SPACE).append(value);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	/**
	 * 拼接leqEquals语句
	 *
	 * @param value 值
	 * @return Conditions
	 */
	public Conditions leqEquals(String columnName, Object value) {
		if (null == value) {
			return this;
		}
		StringBuilder sb = new StringBuilder(columnName).append(SPACE).append(LEQ_EQUALS).append(SPACE).append(value);
		// where判断
		if (!hasWhere) {
			this.hasWhere = true;
			this.sql.append(SPACE).append(OPERATION_WHERE).append(SPACE).append(sb.append(SPACE));
		} else {
			this.sql.append(SPACE).append(OPERATION_AND).append(SPACE).append(sb.append(SPACE));
		}
		return this;
	}

	public StringBuilder getSql() {
		return sql;
	}

	public void setSql(StringBuilder sql) {
		this.sql = sql;
	}

	public boolean isHasWhere() {
		return hasWhere;
	}

	public void setHasWhere(boolean hasWhere) {
		this.hasWhere = hasWhere;
	}

	@Override
	public String toString() {
		if (null == this.sql) {
			return "";
		}
		return this.sql.toString();
	}

}
