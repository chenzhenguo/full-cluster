package org.study.pool.apache;

import java.io.Serializable;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @Title: MyKeyPoolableFactory.java
 * @Description: TODO KeyedPooledObjectFactory
 * @author zhaotf
 * @date 2018年2月25日 上午11:16:41
 * @see {@linkplain http://blog.csdn.net/xlxxcc/article/details/52402931}
 */
public class MyKeyPoolableFactory implements KeyedPooledObjectFactory<Serializable, BaseObject> {
	/**
	 * 创建一个实例到对象池
	 */
	@Override
	public PooledObject<BaseObject> makeObject(Serializable key) throws Exception {
		// 这里从数据库里查询出使用次数最少的配置
		BaseObject bo = new BaseObject();
		bo.setNum(0);
		PooledObject<BaseObject> po = new DefaultPooledObject<BaseObject>(bo);
		System.out.println("创建一个实例到对象池:" + key);
		return po;
	}

	/**
	 * 销毁被破坏的实例
	 */
	@Override
	public void destroyObject(Serializable key, PooledObject<BaseObject> p) throws Exception {
		// BaseObject bo = p.getObject();
		// bo = null;
		p = null;
	}

	/**
	 * 验证该实例是否安全 true:正在使用
	 */
	@Override
	public boolean validateObject(Serializable key, PooledObject<BaseObject> p) {
		// 这里可以判断实例状态是否可用
		if (p.getObject().isActive()) {
			return true;
		}
		return false;
	}

	/**
	 * 重新初始化实例返回池
	 */
	@Override
	public void activateObject(Serializable key, PooledObject<BaseObject> p) throws Exception {
		p.getObject().setActive(true);
	}

	/**
	 * 取消初始化实例返回到空闲对象池
	 */
	@Override
	public void passivateObject(Serializable key, PooledObject<BaseObject> p) throws Exception {
		p.getObject().setActive(false);
	}

}
