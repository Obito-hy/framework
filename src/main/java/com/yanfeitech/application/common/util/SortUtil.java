package com.yanfeitech.application.common.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>
 * Title: SortUtil
 * </p>
 * <p>
 * Description: 排序工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class SortUtil<T> {

	/**
	 * @param targetList 要排序的实体类List集合
	 * @param sortField  排序字段(实体类属性名)
	 * @param sortMode   true正序，false逆序
	 */
	@SuppressWarnings("all")
	public static <T> void sort(List<T> targetList, final String sortField, final boolean sortMode) {
		if (targetList == null || targetList.size() < 2 || sortField == null || sortField.length() == 0) {
			return;
		}
		Collections.sort(targetList, new Comparator() {
			@Override
			public int compare(Object obj1, Object obj2) {
				if (obj1 == null) {
					return 1;
				}
				if (obj2 == null) {
					return -1;
				}
				int retVal = 0;
				try {
					String methodStr = "get" + sortField.substring(0, 1).toUpperCase() + sortField.substring(1);
					Method method1 = ((T) obj1).getClass().getMethod(methodStr, null);
					Method method2 = ((T) obj2).getClass().getMethod(methodStr, null);

					Object o1 = method1.invoke(((T) obj1), null);
					Object o2 = method1.invoke(((T) obj2), null);
					if (o1 == null) {
						return 1;
					}
					if (o2 == null) {
						return -1;
					}

					Type classType = method1.getGenericReturnType();

					// 因为java.util.date时间格式默认以星期开头，不符合排序规则，先将其格式化，在进行比对
					if (classType.equals(Date.class)) {
						o1 = CommonUtil.dateToTimeString((Date) o1);
						o2 = CommonUtil.dateToTimeString((Date) o2);
					}

					if (classType.equals(Integer.class) || classType.equals(int.class)) {
						if (sortMode) {
							retVal = (int) o1 - (int) o2;
						} else {
							retVal = (int) o2 - (int) o1;
						}
					} else if (classType.equals(BigDecimal.class)) {
						if (sortMode) {
							retVal = ((BigDecimal) o1).compareTo((BigDecimal) o2);
						} else {
							retVal = ((BigDecimal) o2).compareTo((BigDecimal) o1);
						}
					} else {
						if (sortMode) {
							retVal = o1.toString().compareTo(o2.toString());
						} else {
							retVal = o2.toString().compareTo(o1.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retVal;
			}
		});
	}

	/**
	 * @param targetList 要排序的实体类List集合
	 * @param sortMode   true正序，false逆序
	 */
	@SuppressWarnings("all")
	public static <T> void sort(List<T> targetList, final boolean sortMode) {
		if (targetList == null || targetList.size() < 2) {
			return;
		}
		Collections.sort(targetList, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				int retVal = 0;
				try {
					Type classType = targetList.get(0).getClass();

					// 因为java.util.date时间格式默认以星期开头，不符合排序规则，先将其格式化，在进行比对
					if (classType.equals(Date.class)) {
						o1 = CommonUtil.dateToTimeString((Date) o1);
						o2 = CommonUtil.dateToTimeString((Date) o2);
					}

					if (classType.equals(Integer.class) || classType.equals(int.class)) {
						if (sortMode) {
							retVal = (int) o1 - (int) o2;
						} else {
							retVal = (int) o2 - (int) o1;
						}
					} else if (classType.equals(BigDecimal.class)) {
						if (sortMode) {
							retVal = ((BigDecimal) o1).compareTo((BigDecimal) o2);
						} else {
							retVal = ((BigDecimal) o2).compareTo((BigDecimal) o1);
						}
					} else {
						if (sortMode) {
							retVal = o1.toString().compareTo(o2.toString());
						} else {
							retVal = o2.toString().compareTo(o1.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return retVal;
			}
		});
	}
}
