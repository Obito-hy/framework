package com.yanfeitech.application.dao.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.query.NativeQuery;

import com.yanfeitech.application.common.page.PageResult;

/**
 * 
 * <p>
 * Title: IBaseDao
 * </p>
 * <p>
 * Description: DAO基础接口
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
public interface IBaseDao<T, ID extends Serializable> {

	/**
	 * 实例化SQLQuery
	 *
	 * @param sql 原生sql语句
	 * @return SQLQuery
	 */
	NativeQuery<T> createSQLQuery(String sql);

	/**
	 * 保存实体 实体类中不再进行WHO字段的初始化（因为更新操作时，SpringMVC会重新初始化WHO字段，导致时间戳被覆盖）
	 *
	 * @param t 待保存的实体
	 */
	void save(T t);

	/**
	 * 保存或者更新实体
	 *
	 * @param t 实体
	 */
	void saveOrUpdate(T t);

	/**
	 * 加载实体的load方法（延迟加载，仅返回包含ID属性的代理对象）
	 *
	 * @param id 实体的id
	 * @return 查询出来的实体
	 */
	T load(ID id);

	/**
	 * 查找的get方法（直接查询数据库，返回具体对象）
	 *
	 * @param id 实体的id
	 * @return 查询出来的实体
	 */
	T get(ID id);

	/**
	 * 根据ID删除数据
	 *
	 * @param Id 实体id
	 * @return 是否删除成功
	 */
	void deleteById(ID id);

	/**
	 * 删除所有数据
	 *
	 * @param entities 待删除实体的Collection集合
	 */
	void deleteAll(Collection<T> entities);

	/**
	 * 通用的原生SQL查询<br>
	 * 根据SQL语句查找唯一实体
	 *
	 * @param <V>    参数名的类型，一般是String
	 * @param <K>    参数值的类型，一般是Object
	 * @param sql    SQL语句，查询列必须使用 as 别名，否则查询结果不能映射到实体
	 * @param params 参数键值对，只能是命名参数
	 * @return 查询实体
	 */
	<K, V> T getBySql(String sql, Map<K, V> params);

	/**
	 * @param sql
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	T getBySql(String sql, String paramName, Object paramValue);

	/**
	 * 通用的原生SQL查询<br>
	 * 根据SQL语句查找实体集合
	 *
	 * @param <V>    参数名的类型，一般是String
	 * @param <K>    参数值的类型，一般是Object
	 * @param sql    SQL语句，查询列必须使用 as 别名，否则查询结果不能映射到实体
	 * @param params 参数键值对，只能是命名参数
	 * @return 实体的List集合
	 */
	<K, V> List<T> getListBySql(String sql, Map<K, V> params);

	@SuppressWarnings("rawtypes")
	<K, V> List<?> getListBySql(String sql, Map<K, V> params, Class cls);

	List<T> getListBySql(String sql, String paramName, Object paramValue);

	@SuppressWarnings("rawtypes")
	List<?> getListBySql(String sql, String paramName, Object paramValue, Class cls);

	/**
	 * 更新实体
	 *
	 * @param t 待更新的实体
	 */
	void update(T t);

	/**
	 * 批量更新实体
	 *
	 * @param entities 待更新的实体集合
	 */
	void updateBatch(List<T> entities);

	/**
	 * 根据SQL得到记录数
	 *
	 * @param sql    SQL语句
	 * @param params 参数键值对，只能是命名参数
	 * @return 记录总数
	 */
	<K, V> int countBySql(String sql, Map<K, V> params);

	/**
	 * SQL分页查询（不需要总数，该分页查询内部会自动计算总数，以后的分页查询统一使用这个接口）
	 *
	 * @param sql      SQL语句
	 * @param pageNo   页码
	 * @param pageSize 一页总条数
	 * @param cls      封装的结果集类型
	 * @return PageResult的封装类，里面包含了页码的信息以及查询的数据List集合
	 */
	@SuppressWarnings("rawtypes")
	<K, V> PageResult<?> findPageBySql(String sql, int pageNo, int pageSize, Class cls);

	/**
	 * SQL分页查询（不需要总数，该分页查询内部会自动计算总数，以后的分页查询统一使用这个接口）
	 *
	 * @param sql      SQL语句
	 * @param pageNo   页码
	 * @param pageSize 一页总条数
	 * @param params   参数键值对，只能是命名参数
	 * @param cls      封装的结果集类型
	 * @return PageResult的封装类，里面包含了页码的信息以及查询的数据List集合
	 */
	@SuppressWarnings("rawtypes")
	<K, V> PageResult<?> findPageBySql(String sql, int pageNo, int pageSize, Class cls, Map<K, V> params);

	/**
	 * @param sql
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	int countBySql(String sql, String paramName, Object paramValue);

	@SuppressWarnings("rawtypes")
	Object getBySql(String sql, String paramName, Object paramValue, Class cls);

	@SuppressWarnings("rawtypes")
	<K, V> Object getBySql(String sql, Map<K, V> params, Class cls);

	/**
	 * 批量保存
	 *
	 * @param entities 待保存实体的集合
	 */
	void save(List<T> entities);

	/**
	 * 根据SQL更新数据库表的指定字段
	 *
	 * @param entity     数据库表对应的实体
	 * @param sql        数据库的UPDATE语句
	 * @param fieldNames 实体的属性名数组（通过属性名的值，更新数据库表的指定字段）
	 */
	<E> void updateBySql(E entity, String sql, String... fieldNames);

	/**
	 * 根据SQL批量更新数据库表的指定字段
	 *
	 * @param entities   数据库表对应的实体集合
	 * @param sql        数据库的UPDATE语句
	 * @param fieldNames 实体的属性名数组（通过属性名的值，更新数据库表的指定字段）
	 */
	<E> void updateAllBySql(List<E> entities, String sql, String... fieldNames);

}
