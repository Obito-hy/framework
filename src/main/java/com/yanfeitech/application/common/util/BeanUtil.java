package com.yanfeitech.application.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cglib.beans.BeanCopier;

/**
 * 
 * <p>
 * Title: BeanUtil
 * </p>
 * <p>
 * Description: 对象/列表深拷贝工具类
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public class BeanUtil {

	/**
	 * 单个对象属性拷贝
	 * 
	 * @param source 源对象
	 * @param clazz  目标对象Class
	 * @param <T>    目标对象类型
	 * @return 目标对象
	 */
	public static <T, M> T copyProperties(M source, Class<T> clazz) {
		if (Objects.isNull(source) || Objects.isNull(clazz))
			throw new IllegalArgumentException();
		return copyProperties(source, clazz, null);
	}

	/**
	 * 列表对象拷贝
	 * 
	 * @param sources 源列表
	 * @param clazz   源列表对象Class
	 * @param <T>     目标列表对象类型
	 * @return 目标列表
	 */
	public static <T, M> List<T> copyList(List<M> sources, Class<T> clazz) {
		if (Objects.isNull(sources) || Objects.isNull(clazz) || sources.isEmpty())
			throw new IllegalArgumentException();
		BeanCopier copier = BeanCopier.create(sources.get(0).getClass(), clazz, false);
		return Optional.of(sources).orElse(new ArrayList<>()).stream().map(m -> copyProperties(m, clazz, copier))
				.collect(Collectors.toList());
	}

	/**
	 * 单个对象属性拷贝
	 * 
	 * @param source 源对象
	 * @param clazz  目标对象Class
	 * @param copier copier
	 * @param <T>    目标对象类型
	 * @param <M>    源对象类型
	 * @return 目标对象
	 */
	@SuppressWarnings("deprecation")
	private static <T, M> T copyProperties(M source, Class<T> clazz, BeanCopier copier) {
		if (null == copier) {
			copier = BeanCopier.create(source.getClass(), clazz, false);
		}
		T t = null;
		try {
			t = clazz.newInstance();
			copier.copy(source, t, null);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
}
