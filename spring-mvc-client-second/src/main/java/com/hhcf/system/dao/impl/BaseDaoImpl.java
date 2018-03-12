package com.hhcf.system.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.hhcf.system.dao.BaseDao;

/**
 * 
 * @Title: BaseDaoImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月12日 下午2:14:25
 */
@Repository("baseDao")
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {
	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);
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

	@Override
	public <T> T getEntity(Class entityName, Serializable id) {
		T t = (T) getCurrentSession().get(entityName, id);
		if (t != null) {
			getCurrentSession().flush();
		}
		return t;
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void batchSaveOrUpdate(List<T> entitys) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findMapListForJdbc(String sql, Object[] objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getMapForJdbc(String sql, Object[] objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
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
	public List<Map<String, Object>> findMapListForPage(String sql, int start, int totalResults, Object[] objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findMapListForPage(String sql, Map<String, Object> params, int start,
			int totalResults) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getCountForJdbc(String sql) {
		// TODO Auto-generated method stub
		return null;
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
						entity.getClass().getMethod("setUpdateuser", String.class).invoke(entity, "0.0.0.0");
					}
				}
				if (method.getName().equals("setUpdateip")) {
					method.invoke(entity, "0.0.0.0");
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
						entity.getClass().getMethod("setInsertuser", String.class).invoke(entity, "0.0.0.0");
					}
				}
				if (method.getName().equals("getInsertip")) {
					String val = (String) method.invoke(entity);
					if (StringUtils.isBlank(val)) {
						entity.getClass().getMethod("setInsertip", String.class).invoke(entity, "0.0.0.0");
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
}
