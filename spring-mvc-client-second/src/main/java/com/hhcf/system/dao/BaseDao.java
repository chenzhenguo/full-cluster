package com.hhcf.system.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @Title: BaseDao
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月12日 下午2:13:45
 */
public interface BaseDao {
	/**
	 * 根据主键ID查询实体对象
	 * 
	 * @param entityName
	 *            实体类名
	 * @param id
	 *            主键ID
	 * @return
	 */
	public <T> T getEntity(Class entityName, Serializable id);

	/**
	 * 按属性查找对象列表.
	 * 
	 * @param entityClass
	 *            实体类名
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            字段值
	 * @return
	 */
	public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

	/**
	 * 保存实体类
	 * 
	 * @param entity
	 * @return
	 */
	public <T> Serializable save(T entity);

	/**
	 * 更新并保存实体类
	 * 
	 * @param entity
	 */
	public <T> void saveOrUpdate(T entity);

	/**
	 * 删除实体类
	 * 
	 * @param entitie
	 */
	public <T> void delete(T entitie);

	/**
	 * 通过属性称获取实体带排序
	 * 
	 * @param <T>
	 * @param clas
	 * @return
	 */
	public <T> List<T> findByPropertyisOrder(Class<T> entityClass, String propertyName, Object value, boolean isAsc);

	/**
	 * 批量修改并保存
	 * 
	 * @param entitys
	 */
	public <T> void batchSaveOrUpdate(List<T> entitys);

	/**
	 * 根据实体名字获取唯一记录
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);

	/**
	 * 获取定制类型数据集合(list map)
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 */
	public List<Map<String, Object>> findForJdbc(String sql, Object... objs);

	/**
	 * 获取定制类型数据集合(list map)
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 * @Description:
	 */
	public List<Map<String, Object>> findMapListForJdbc(String sql, Object[] objs);

	/**
	 * 获取单个定制化对象
	 * 
	 * @param sql
	 * @param objs
	 * @return
	 * @Description:
	 */
	public Map<String, Object> getMapForJdbc(String sql, Object[] objs);

	/**
	 * 定时任务自动执行保存实体方法
	 * 
	 * @param entity
	 * @return
	 */
	public <T> Serializable saveByTask(T entity);

	/**
	 * 分页，定制化查询
	 * 
	 * @param sql
	 * @param start
	 * @param totalResults
	 * @param objs
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findMapListForPage(String sql, int start, int totalResults, Object[] objs);

	/**
	 * 
	 * @param sql
	 * @param params
	 *            查询参数集合
	 * @param start
	 * @param totalResults
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findMapListForPage(String sql, Map<String, Object> params, int start,
			int totalResults);

	Long getCountForJdbc(String sql);

}
