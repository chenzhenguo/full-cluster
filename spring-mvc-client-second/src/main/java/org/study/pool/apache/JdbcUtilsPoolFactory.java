package org.study.pool.apache;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * @Title: JdbcUtilsPoolFactory
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月24日 上午8:32:37
 * @see {@linkplain http://blog.csdn.net/xlxxcc/article/details/52402931}
 */
public class JdbcUtilsPoolFactory extends BasePooledObjectFactory<JdbcUtils>{

	@Override
	public JdbcUtils create() throws Exception {
		return null;
	}

	@Override
	public PooledObject<JdbcUtils> wrap(JdbcUtils obj) {
		// TODO Auto-generated method stub  
		return null;
	}

}
