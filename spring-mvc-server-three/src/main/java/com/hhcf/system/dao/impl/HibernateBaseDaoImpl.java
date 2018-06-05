package com.hhcf.system.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.ws.RespectBinding;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.hhcf.system.dao.HibernateBaseDao;

/**
 * 
 * @Title: HibernateBaseDaoImpl
 * @Description:hibernate基类DAO
 * @Author: zhaotf
 * @Since:2018年6月5日 下午1:50:44
 */
@Repository("hibernateBaseDao")
public class HibernateBaseDaoImpl extends HibernateDaoSupport implements HibernateBaseDao {

	/**
	 * 注入一个sessionFactory属性,并注入到父类(HibernateDaoSupport)
	 **/
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public Session getCurrentSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	public void setSessionFactoryOverride(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	/**
	 * 根据主键获取实体并加锁。
	 * 
	 * @param <T>
	 * @param entityName
	 * @param id
	 * @param lock
	 * @return
	 */
	public <T> T getEntity(Class entityName, Serializable id) {
		T t = (T) getCurrentSession().get(entityName, id);
		if (t != null) {
			getCurrentSession().flush();
		}
		return t;
	}

	/**
	 * 按属性查找对象列表.
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (List<T>) createCriteria(entityClass, Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 创建Criteria对象带属性比较
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass, Criterion... criterions) {
		Criteria criteria = getCurrentSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 根据传入的实体持久化对象
	 */
	public <T> Serializable save(T entity) {
		try {
			this.setInsertInfo(entity);
			Serializable id = getCurrentSession().save(entity);
			getCurrentSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("保存实体成功," + entity.getClass().getName());
			}
			return id;
		} catch (RuntimeException e) {
			logger.error("保存实体异常", e);
			throw e;
		}
	}

	/**
	 * 设置对象生成/更新信息
	 * 
	 * @param entity
	 */
	public <T> void setInsertInfo(T entity) {
		try {
			// 得到类中的方法
			Method[] methods = entity.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (method.getName().equals("getUpdateuser")) {
					String val = (String) method.invoke(entity);
					if (StringUtils.isBlank(val)) {
						entity.getClass().getMethod("setUpdateuser", String.class).invoke(entity, IpUtil.getLocalIp());
					}
				}
				if (method.getName().equals("setUpdateip")) {
					method.invoke(entity, IpUtil.getLocalIp());
				}
				if (method.getName().equals("setUpdatetime")) {
					method.invoke(entity, new Date());
				}

				if (method.getName().equals("getInserttime")) {
					Object time = method.invoke(entity);
					if (time == null) {
						entity.getClass().getMethod("setInserttime", Date.class).invoke(entity, new Date());
					}
				}
				if (method.getName().equals("getInsertuser")) {
					String val = (String) method.invoke(entity);
					if (StringUtils.isBlank(val)) {
						entity.getClass().getMethod("setInsertuser", String.class).invoke(entity, IpUtil.getLocalIp());
					}
				}
				if (method.getName().equals("getInsertip")) {
					String val = (String) method.invoke(entity);
					if (StringUtils.isBlank(val)) {
						entity.getClass().getMethod("setInsertip", String.class).invoke(entity, IpUtil.getLocalIp());
					}
				}
				if (method.getName().equals("getDeleteflag")) {
					String val = (String) method.invoke(entity);
					if (StringUtils.isBlank(val)) {
						entity.getClass().getMethod("setDeleteflag", String.class).invoke(entity, "0");
					}
				}
			}
		} catch (Exception e) {
			logger.error("设置实体生成信息异常", e);
		}
	}

	/**
	 * 根据传入的实体添加或更新对象
	 * 
	 * @param <T>
	 * 
	 * @param entity
	 */
	public <T> void saveOrUpdate(T entity) {
		try {
			this.setInsertInfo(entity);
			getCurrentSession().saveOrUpdate(entity);
			getCurrentSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("添加或更新成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("添加或更新异常", e);
			throw e;
		}
	}

	/**
	 * 根据传入的实体删除对象
	 */
	public <T> void delete(T entity) {
		try {
			getCurrentSession().delete(entity);
			getCurrentSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("删除成功," + entity.getClass().getName());
			}
		} catch (RuntimeException e) {
			logger.error("删除异常", e);
			throw e;
		}
	}

	/**
	 * 根据属性名和属性值查询. 有排序
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param propertyName
	 * @param value
	 * @param orderBy
	 * @param isAsc
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc) {
		Assert.hasText(propertyName);
		return createCriteria(entityClass, isAsc, Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 创建Criteria对象，有排序功能。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param orderBy
	 * @param isAsc
	 * @param criterions
	 * @return
	 */
	private <T> Criteria createCriteria(Class<T> entityClass, boolean isAsc, Criterion... criterions) {
		Criteria criteria = createCriteria(entityClass, criterions);
		if (isAsc) {
			criteria.addOrder(Order.asc("asc"));
		} else {
			criteria.addOrder(Order.desc("desc"));
		}
		return criteria;
	}

	/**
	 * 根据传入的实体批量添加或更新对象
	 * 
	 * @param <T>
	 * 
	 * @param entity
	 */
	public <T> void batchSaveOrUpdate(List<T> entitys) {
		for (int i = 0; i < entitys.size(); i++) {
			getCurrentSession().saveOrUpdate(entitys.get(i));
			if (i % 20 == 0) {
				// 20个对象后才清理缓存，写入数据库
				getCurrentSession().flush();
				getCurrentSession().clear();
			}
		}
		// 最后清理一下----防止大于20小于40的不保存
		getCurrentSession().flush();
		getCurrentSession().clear();
	}

	/**
	 * 执行HQL语句,适合增删改 (由 mayi 新增的方法)
	 * 
	 * @param hql
	 * @param values
	 * @return
	 * @throws Exception
	 */
	protected Integer excuteHql(final String hql, Object... values) throws Exception {
		int count = 0;
		Session session = null;
		try {
			session = getHibernateTemplate().getSessionFactory().openSession();
			Query query = session.createQuery(hql);
			if (values != null && values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					query.setParameter(i, values[i]);
				}
			}
			count = query.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return count;
	}

	/**
	 * 根据实体名字获取唯一记录
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(entityClass, Restrictions.eq(propertyName, value)).uniqueResult();
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		return this.jdbcTemplate.queryForList(sql, objs);
	}

	@Override
	public Map<String, Object> getMapForJdbc(String sql, Object... objs) {
		Query query = getCurrentSession().createSQLQuery(sql);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		return (Map<String, Object>) query.uniqueResult();
	}

	@Override
	public List<Map<String, Object>> findMapListForJdbc(String sql, Object... objs) {
		Query query = getCurrentSession().createSQLQuery(sql);
		this.setArrayParams(query, objs);// 设置查询参数
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.list();
		return list;
	}

	/**
	 * @param pageNo
	 *            页码
	 * @param totalResults
	 *            单次查询最大条数
	 */
	@Override
	public List<Map<String, Object>> findMapListForPage(String sql, int pageNo, int totalResults, Object... objs) {
//		sql = JdbcDao.jeecgCreatePageSql(sql, pageNo, totalResults);
		Query query = getCurrentSession().createSQLQuery(sql);
		this.setArrayParams(query, objs);// 设置查询参数
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	/**
	 * 设置查询参数
	 * 
	 * @param query
	 * @param objs
	 *            void
	 */
	public void setArrayParams(Query query, Object... objs) {
		if (objs != null && objs.length > 0) {
			for (int i = 0; i < objs.length; i++) {
				query.setParameter(i, objs[i]);
			}
		}
	}

	/**
	 * @param start
	 *            起始查询行数位置
	 * @param totalResults
	 *            单次查询最大条数
	 */
	@Override
	public List<Map<String, Object>> findMapListForPage(String sql, Map<String, Object> params, int start,
			int totalResults) {
		Query query = getCurrentSession().createSQLQuery(sql);
		query = getSqlQueryByMap(query, params);
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		return query.setFirstResult(start).setMaxResults(totalResults).list();
	}

	public Query getSqlQueryByMap(Query sqlQuery, Map<String, Object> params) {
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object obj = params.get(key);
				if (obj instanceof Collection<?>)
					sqlQuery.setParameterList(key, (Collection<?>) obj);
				else if (obj instanceof Object[])
					sqlQuery.setParameterList(key, (Object[]) obj);
				else
					sqlQuery.setParameter(key, obj);

			}
		}
		return sqlQuery;
	}

	/**
	 * 定时任务自动执行保存实体方法
	 * 
	 * @param entity
	 * @return
	 */
	public <T> Serializable saveByTask(T entity) {
		try {
			this.setInsertInfo(entity);
			Serializable id = getCurrentSession().save(entity);
			getCurrentSession().flush();
			if (logger.isDebugEnabled()) {
				logger.debug("保存实体成功," + entity.getClass().getName());
			}
			return id;
		} catch (RuntimeException e) {
			logger.error("保存实体异常", e);
			throw e;
		}
	}

	@Override
	public Long getCountForJdbc(String sql) {
		Query query = getCurrentSession().createSQLQuery(sql);
		return ((BigInteger) query.uniqueResult()).longValue();
	}
}
