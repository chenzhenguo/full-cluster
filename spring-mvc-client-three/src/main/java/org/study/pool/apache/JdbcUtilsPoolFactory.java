package org.study.pool.apache;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * @Title: JdbcUtilsPoolFactory
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月24日 上午8:32:37
 * @see {@linkplain http://blog.csdn.net/xlxxcc/article/details/52402931}
 */
public class JdbcUtilsPoolFactory extends BasePooledObjectFactory<JdbcUtils> {
	public static void main(String[] args) throws Exception {
		JdbcUtils jdbcUtils = JdbcUtilsPoolFactory.borrowObject();
//		jdbcUtils.domestring();
		JdbcUtilsPoolFactory.returnObject(jdbcUtils);
	}

	static GenericObjectPool<JdbcUtils> pool = null;

	// 取得对象池工厂实例
	public synchronized static GenericObjectPool<JdbcUtils> getInstance() {
		if (pool == null) {
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setMaxIdle(-1);
			poolConfig.setMaxTotal(-1);
			poolConfig.setMinIdle(100);
			poolConfig.setLifo(false);
			pool = new GenericObjectPool<JdbcUtils>(new JdbcUtilsPoolFactory(), poolConfig);
		}
		return pool;
	}

	public static JdbcUtils borrowObject() throws Exception {
		return (JdbcUtils) JdbcUtilsPoolFactory.getInstance().borrowObject();
	}

	public static void returnObject(JdbcUtils jdbcUtils) throws Exception {
		JdbcUtilsPoolFactory.getInstance().returnObject(jdbcUtils);
	}

	public static void close() throws Exception {
		JdbcUtilsPoolFactory.getInstance().close();
	}

	public static void clear() throws Exception {
		JdbcUtilsPoolFactory.getInstance().clear();
	}

	@Override
	public JdbcUtils create() throws Exception {
		return null;
	}

	@Override
	public PooledObject<JdbcUtils> wrap(JdbcUtils obj) {
		return new DefaultPooledObject<JdbcUtils>(obj);
	}

}
