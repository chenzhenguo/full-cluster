package org.study.pool.apache;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

import com.hhcf.backend.model.UserModel;

/**
 * @Title: ConnectionTestFactory.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月25日 下午12:11:00
 * @see {@linkplain https://www.jianshu.com/p/b0189e01de35}
 */
public class ConnectionTestFactory extends BaseKeyedPooledObjectFactory<String, UserModel> {
	// private static final Logger logger =
	// LoggerFactory.getLogger(ConnectionTestFactory.class);

	public static void main(String[] args) {
		KeyedObjectPool<String, UserModel> pool = new GenericKeyedObjectPool<String, UserModel>(
				new ConnectionTestFactory());
		try {
			// 添加对象到池，重复的不会重复入池
			pool.addObject("a1");
			pool.addObject("a2");
			pool.addObject("a3");

			// 获得对应key的对象
			UserModel user = pool.borrowObject("a2");
			// logger.info("borrowObject = {}", user);
			System.out.println("borrowObject = " + user);

			// 释放对象
			pool.returnObject("a2", user);

			// 清除所有的对象
			pool.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.close();
		}
	}

	@Override
	public UserModel create(String key) throws Exception {
		UserModel user = new UserModel();
		user.setName(key);
		return user;
	}

	@Override
	public PooledObject<UserModel> wrap(UserModel value) {
		return new DefaultPooledObject<UserModel>(value);
	}

}
