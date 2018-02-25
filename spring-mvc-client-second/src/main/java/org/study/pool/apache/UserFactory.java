package org.study.pool.apache;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.hhcf.backend.model.UserModel;

/**
 * @Title: UserFactory.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月25日 上午11:52:49
 * @see {@linkplain http://blog.csdn.net/m47838704/article/details/51354807}
 */
public class UserFactory implements PooledObjectFactory<UserModel> {

	/**
	 * 
	 */
	public static void main(String[] args) {
		GenericObjectPool<UserModel> pool = new GenericObjectPool<UserModel>(new UserFactory());
		try {
			UserModel user = pool.borrowObject();
			user.setAge(10);
			user.setName("mh");
			System.out.println(user.toString());
			pool.returnObject(user);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.close();
		}

	}

	@Override
	public PooledObject<UserModel> makeObject() throws Exception {
		System.out.println("创建一个新的对象");
		return new DefaultPooledObject<UserModel>(new UserModel());
	}

	@Override
	public void destroyObject(PooledObject<UserModel> p) throws Exception {
		// UserModel user = p.getObject();
		// System.out.println("销毁对象"+user.toString());
		// user = null;
		p = null;
	}

	@Override
	public boolean validateObject(PooledObject<UserModel> p) {
		if (p.getObject() instanceof UserModel) {
			System.out.println("是一个合法的对象");
			return true;
		}
		System.out.println("是一个非法的对象");
		return false;
	}

	@Override
	public void activateObject(PooledObject<UserModel> p) throws Exception {
		System.out.println("重新初始化对象");
	}

	@Override
	public void passivateObject(PooledObject<UserModel> p) throws Exception {
		// TODO Auto-generated method stub
		UserModel user = p.getObject();
		System.out.println("对象已经被归还:" + user.toString());
	}

}
