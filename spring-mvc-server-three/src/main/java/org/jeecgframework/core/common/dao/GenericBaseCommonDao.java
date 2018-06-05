package org.jeecgframework.core.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.DetachedCriteria;

/**
 * 
 * @Title: GenericBaseCommonDao
 * @Description:DAO层泛型基类
 * @Author: zhaotf
 * @Since:2018年6月5日 下午2:37:03
 */
@SuppressWarnings("hiding")
public class GenericBaseCommonDao<T, PK extends Serializable> implements IGenericBaseCommonDao {

	@Override
	public Integer getAllDbTableSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Serializable save(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void batchSave(List<T> entitys) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void delete(T entitie) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T get(Class<T> entityName, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> loadAll(Class<T> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getEntity(Class entityName, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void deleteEntityById(Class entityName, Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void deleteAllEntitie(Collection<T> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void updateEntitie(T pojo) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void updateEntityById(Class entityName, Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> List<T> findByQueryString(String hql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T singleResult(String hql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateBySqlString(String sql) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> List<T> findListbySql(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getListByCriteriaQuery(CriteriaQuery cq, Boolean ispage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List findByExample(String entityName, Object exampleEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Object, Object> getHashMapbyQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getDataGridReturn(CriteriaQuery cq, boolean isOffset) {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer executeSql(String sql, List<Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer executeSql(String sql, Object... param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer executeSql(String sql, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getCountForJdbc(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getCountForJdbcParam(String sql, Object[] objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findHql(String hql, Object... param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer executeHql(String hql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> findByDetached(DetachedCriteria dc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> executeProcedure(String procedureSql, Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
