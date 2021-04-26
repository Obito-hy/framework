package com.yanfeitech.application.dao.config;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yanfeitech.application.common.page.PageResult;

/**
 * 
 * <p>
 * Title: BaseDao
 * </p>
 * <p>
 * Description: DAO基类，封装一些常用的CRUD方法
 * </p>
 * 
 * @author zhudelin
 * @date 2020年11月24日
 */
@SuppressWarnings("all")
@Component
public class BaseDao<T, ID extends Serializable> implements IBaseDao<T, ID> {

	@Autowired
	private EntityManager entityManager;

	private Class<T> entityClass;

	public BaseDao() {

	}

	private Class<T> getEntityClass() {
		if (null == entityClass) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
		return entityClass;
	}

	public Session getSession() {
		Session session = (Session) entityManager.getDelegate();
		return session;
	}

	@Override
	public NativeQuery<T> createSQLQuery(String sql) {
		return getSession().createSQLQuery(sql);
	}

	/**
	 * 实体类中不再进行WHO字段的初始化（因为更新操作时，SpringMVC会重新初始化WHO字段，导致时间戳被覆盖）
	 */
	@Override
	public void save(T t) {
		getSession().save(t);
	}

	public void saveOne(Object obj) {
		getSession().save(obj);
	}

	@Override
	public void save(List<T> entities) {
		for (int i = 0; i < entities.size(); i++) {
			getSession().save(entities.get(i));
			if (0 == (i % 10)) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	public void saveBatch(List<?> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			return;
		}
		int count = entities.size();
		for (int i = 0; i < count; i++) {
			getSession().save(entities.get(i));
			if (0 == (i % 15)) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	@Override
	public void saveOrUpdate(T t) {
		getSession().saveOrUpdate(t);
	}

	@Override
	public T load(ID id) {
		T entity = (T) getSession().load(getEntityClass(), id);
		return entity;
	}

	@Override
	public T get(ID id) {
		T entity = (T) getSession().get(getEntityClass(), id);
		return entity;
	}

	/**
	 * 不可物理删除数据<br>
	 * 删除操作只是修改了数据的状态，同时更新记录的who字段
	 *
	 * @param id 主键
	 * @param t  待更新的数据（不需要更新的属性，值保持null即可）
	 */
	@Override
	public void deleteById(ID id) {
		T dbEntity = get(id);
		getSession().delete(dbEntity);
	}

	/**
	 * 批量删除（同样是更新操作）<br>
	 * 该批量删除，只支持主键名为id的实体
	 *
	 * @param entities 待删除的实体集合
	 */
	@Override
	public void deleteAll(Collection<T> entities) {
		for (T t : entities) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor("id", t.getClass());
				Method getMethod = pd.getReadMethod();
				Object pkValue = getMethod.invoke(t);
				deleteById((ID) pkValue);
			} catch (IntrospectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public <E> void updateBySql(E entity, String sql, String... fieldNames) {
		List<E> entities = new ArrayList<>();
		entities.add(entity);
		updateAllBySql(entities, sql, fieldNames);
	}

	/**
	 * 使用SQL语句执行更新操作（单条更新）
	 *
	 * @param sql    更新的SQL语句
	 * @param params 参数Map
	 */
	public void updateBySql(String sql, Map<String, Object> params) {
		NativeQuery<T> query = initSqlForListParams(sql, params, null);
		query.executeUpdate();
	}

	private NativeQuery<T> initSqlForListParams(String sql, Map<String, Object> params, Class<T> entityType) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		if (null != params) {
			for (String key : params.keySet()) {
				// 传入的参数是什么类型，不同类型使用的方法不同
				Object value = params.get(key);
				if (value instanceof Collection<?>) {
					query.setParameterList(key, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(key, (Object[]) value);
				} else {
					query.setParameter(key, value);
				}
			}
		}
		if (null != entityType) {
			query.setResultTransformer(new BeanTransformerAdapter<T>(entityType));
		}
		return query;
	}

	@Override
	public <E> void updateAllBySql(List<E> entities, String sql, String... fieldNames) {
		if (null == entities || entities.size() == 0) {
			return;
		}
		int count = 1;
		int commitCount = 100;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (E entity : entities) {
			Session currentSession = getSession();
			NativeQuery<T> query = currentSession.createSQLQuery(sql);
			if (null != fieldNames && fieldNames.length > 0) {
				Class<? extends Object> entityClass = entity.getClass();
				for (String fieldName : fieldNames) {
					try {
						PropertyDescriptor pd = new PropertyDescriptor(fieldName, entityClass);
						Method getMethod = pd.getReadMethod();
						Object fieldValue = getMethod.invoke(entity);
						if (fieldValue instanceof Date) {
							Date date = (Date) fieldValue;
							query.setParameter(fieldName, sdf.format(date));
						} else {
							query.setParameter(fieldName, getMethod.invoke(entity));
						}
					} catch (IntrospectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			query.executeUpdate();
			if (0 == count % commitCount) {
				currentSession.flush();
				currentSession.clear();
			}
			count++;
		}
	}

	@Override
	public <K, V> T getBySql(String sql, Map<K, V> params) {
		NativeQuery<T> query = initSql(sql, params, getEntityClass());
		T entity = (T) query.uniqueResult();
		return entity;
	}

	@Override
	public T getBySql(String sql, String paramName, Object paramValue) {
		NativeQuery<T> query = initSql(sql, paramName, paramValue, getEntityClass());
		T entity = (T) query.uniqueResult();
		return entity;
	}

	@Override
	public <K, V> List<T> getListBySql(String sql, Map<K, V> params) {
		NativeQuery<T> query = initSql(sql, params, getEntityClass());
		List<T> entities = new ArrayList<T>(10);
		entities = query.list();
		return entities;
	}

	public <V, K> List<?> getListBySqlNoClass(String sql, Map<K, V> params) {
		NativeQuery<T> query = initSql(sql, params);
		List<?> entities = new ArrayList<>(10);
		entities = query.list();
		return entities;
	}

	public List<?> getListBySqlNoClass(String sql) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		List<?> entities = new ArrayList<>(10);
		entities = query.list();
		return entities;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <K, V> List<?> getListBySql(String sql, Map<K, V> params, Class cls) {
		NativeQuery<T> query = initSql(sql, params, cls);
		List<?> entities = new ArrayList<>();
		entities = query.list();
		return entities;
	}

	@Override
	public List<T> getListBySql(String sql, String paramName, Object paramValue) {
		NativeQuery<T> query = initSql(sql, paramName, paramValue, getEntityClass());
		List<T> entities = new ArrayList<T>(10);
		entities = query.list();
		return entities;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<?> getListBySql(String sql, String paramName, Object paramValue, Class cls) {
		NativeQuery<T> query = initSql(sql, paramName, paramValue, cls);
		List<?> entities = new ArrayList<>(10);
		entities = query.list();
		return entities;
	}

	@SuppressWarnings("rawtypes")
	public List<?> getListBySql(String sql, Class cls) {
		NativeQuery<T> query = initSql(sql, cls);
		List<?> entities = query.list();
		if (CollectionUtils.isEmpty(entities)) {
			entities = new ArrayList<>();
		}
		return entities;
	}

	@Override
	public void update(T t) {
		getSession().update(t);
	}

	@Override
	public void updateBatch(List<T> entities) {
		for (int i = 0; i < entities.size(); i++) {
			getSession().update(entities.get(i));
			if (0 == (i % 10)) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	@Override
	public <K, V> int countBySql(String sql, Map<K, V> params) {
		NativeQuery<T> query = initSql(sql, params);
		int count = ((Number) query.uniqueResult()).intValue();
		return count;
	}

	public int countBySql(String sql) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		int count = ((Number) query.uniqueResult()).intValue();
		return count;
	}

	@Override
	public int countBySql(String sql, String paramName, Object paramValue) {
		NativeQuery<T> query = initSql(sql, paramName, paramValue);
		int count = ((Number) query.uniqueResult()).intValue();
		return count;
	}

	/**
	 * 把普通SQL语句转化成查询总数的SQL语句（SQL语句如果有group by，那么只能在最外层，否则不支持）
	 *
	 * @param sql
	 * @return Sting 查询总数的SQL语句
	 */
	private String initSqlOfCount(String sql) {
		final String SPACE = " ";
		int selectIndex = sql.indexOf("select");
		int fromIndex = sql.indexOf("from");
		String countSql = "";
		if (sql.contains("group by")) {
			countSql = "select count(1) from (" + sql + ") t";
		} else {
			countSql = sql.substring(selectIndex, 6) + SPACE + "count(1)" + SPACE
					+ sql.substring(fromIndex, sql.length());
		}
		return countSql;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <K, V> PageResult<?> findPageBySql(String sql, int pageNo, int pageSize, Class cls) {
		return findPageBySql(sql, pageNo, pageSize, cls, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <K, V> PageResult<?> findPageBySql(String sql, int pageNo, int pageSize, Class cls, Map<K, V> params) {
		String countSql = initSqlOfCount(sql);
		int totalCount = countBySql(countSql, params);
		PageResult<T> pageResult = new PageResult<T>();
		int currentPage = pageNo > 1 ? pageNo : 1;
		pageResult.setCurrentPage(currentPage);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(totalCount);
		pageResult.resetNextPage();
		NativeQuery<T> query = initSql(sql, params, cls);
		List<T> entities = query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
		if (CollectionUtils.isEmpty(entities)) {
			entities = new ArrayList<>();
		}
		pageResult.setResults(entities);
		return pageResult;
	}

	/**
	 * 
	 * 带总数查分页列表
	 * 
	 * @param <K>
	 * @param <V>
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @param totalCount
	 * @param cls
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <K, V> PageResult<?> findPageCountBySql(String sql, int pageNo, int pageSize, int totalCount, Class cls,
			Map<K, V> params) {
		PageResult<T> pageResult = new PageResult<>();
		int currentPage = pageNo > 1 ? pageNo : 1;
		pageResult.setCurrentPage(currentPage);
		pageResult.setPageSize(pageSize);
		pageResult.setTotal(totalCount);
		pageResult.resetNextPage();
		NativeQuery<T> query = initSql(sql, params, cls);
		List<T> entities = query.setFirstResult((currentPage - 1) * pageSize).setMaxResults(pageSize).list();
		if (CollectionUtils.isEmpty(entities)) {
			entities = new ArrayList<>();
		}
		pageResult.setResults(entities);
		return pageResult;
	}

	/**
	 * 根据命名参数键值对，初始化查询语句（Sql）
	 *
	 * @param sql    查询语句
	 * @param params 命名参数键值对
	 * @return SQLQuery
	 */
	private <K, V> NativeQuery<T> initSql(String sql, Map<K, V> params, Class<T> entityType) {
		NativeQuery<T> query = (NativeQuery<T>) getSession().createSQLQuery(sql);
		if (null != params && params.size() > 0) {
			for (K key : params.keySet()) {
				// 传入的参数是什么类型，不同类型使用的方法不同
				Object value = params.get(key);
				if (value instanceof Collection<?>) {
					query.setParameterList((String) key, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList((String) key, (Object[]) value);
				} else {
					query.setParameter(key.toString(), params.get(key));
				}
			}

		}
		if (null != entityType) {
			query.setResultTransformer(new BeanTransformerAdapter<T>(entityType));
		}
		return query;
	}

	@SuppressWarnings("rawtypes")
	private NativeQuery<T> initSql(String sql, String paramName, Object paramValue, Class entityType) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		query.setParameter(paramName, paramValue);
		query.setResultTransformer(new BeanTransformerAdapter(entityType));
		return query;
	}

	@SuppressWarnings("rawtypes")
	private NativeQuery<T> initSql(String sql, Class entityType) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		query.setResultTransformer(new BeanTransformerAdapter(entityType));
		return query;
	}

	@SuppressWarnings("rawtypes")
	private <K, V> NativeQuery<T> initSql(String sql, Map<K, V> params) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		if (null != params) {
			for (K key : params.keySet()) {
				Object value = params.get(key);
				String name = key.toString();
				if (value instanceof Collection) {
					query.setParameterList(name, (Collection) value);
				} else {
					query.setParameter(name, value);
				}

			}
		}
		return query;
	}

	private <K, V> NativeQuery<T> initSql(String sql, String paramName, Object paramValue) {
		NativeQuery<T> query = getSession().createSQLQuery(sql);
		query.setParameter(paramName, paramValue);
		return query;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getBySql(String sql, String paramName, Object paramValue, Class cls) {
		NativeQuery<T> query = initSql(sql, paramName, paramValue, cls);
		Object object = query.uniqueResult();
		return object;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <K, V> Object getBySql(String sql, Map<K, V> params, Class cls) {
		NativeQuery<T> query = initSql(sql, params, cls);
		Object object = query.uniqueResult();
		return object;
	}

}
