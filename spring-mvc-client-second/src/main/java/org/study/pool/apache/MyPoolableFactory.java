package org.study.pool.apache;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @Title: MyPoolableFactory.java
 * @Description: TODO PooledObjectFactory
 * @author zhaotf
 * @date 2018年2月25日 上午10:48:26
 * @see {@linkplain http://blog.csdn.net/xlxxcc/article/details/52402931}
 */
public class MyPoolableFactory implements PooledObjectFactory<BaseObject> {

	/**
	 * 重新初始化实例返回池
	 */
	@Override
	public void activateObject(PooledObject<BaseObject> arg0) throws Exception {
		// ((BaseObject) arg0).setActive(true);
		arg0.getObject().setActive(true);
	}

	/**
	 * 销毁被破坏的实例
	 */
	@Override
	public void destroyObject(PooledObject<BaseObject> arg0) throws Exception {
		arg0 = null;
	}

	/**
	 * 创建一个实例到对象池
	 */
	@Override
	public PooledObject<BaseObject> makeObject() throws Exception {
		DefaultPooledObject<BaseObject> po = new DefaultPooledObject<BaseObject>(new BaseObject());
		return po;
	}

	/**
	 * 取消初始化实例返回到空闲对象池
	 */
	@Override
	public void passivateObject(PooledObject<BaseObject> arg0) throws Exception {
		arg0.getObject().setActive(false);
	}

	/**
	 * 验证该实例是否安全
	 */
	@Override
	public boolean validateObject(PooledObject<BaseObject> arg0) {
		if (arg0.getObject().isActive()) {
			return true;
		}
		return false;
	}

}
