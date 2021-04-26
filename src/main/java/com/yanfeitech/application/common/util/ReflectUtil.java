package com.yanfeitech.application.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * <p>
 * Title: ReflectUtil
 * </p>
 * <p>
 * Description: 反射常用方法工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class ReflectUtil {

	/**
	 * 反射：获取实体的指定属性的值
	 *
	 * @param t         实体
	 * @param fieldName 属性名
	 * @return Object 属性值
	 */
	public static <T> Object getFieldValue(T t, String fieldName) {
		try {
			Class<? extends Object> clz = t.getClass();
			PropertyDescriptor pd = new PropertyDescriptor(fieldName, clz);
			Method getterMethod = pd.getReadMethod();
			return getterMethod.invoke(t);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 反射：给实体的指定属性赋值
	 *
	 * @param t          实体
	 * @param fieldName  属性名
	 * @param fieldValue 属性值
	 */
	public static <T> void setFieldValue(T t, String fieldName, Object fieldValue) {
		try {
			Class<? extends Object> clz = t.getClass();
			PropertyDescriptor pd = new PropertyDescriptor(fieldName, clz);
			Method setMethod = pd.getWriteMethod();
			setMethod.invoke(t, fieldValue);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
