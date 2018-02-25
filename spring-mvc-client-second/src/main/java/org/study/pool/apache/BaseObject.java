package org.study.pool.apache;

/**
 * @Title: BaseObject.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月25日 上午10:47:32
 * @see {@linkplain http://blog.csdn.net/xlxxcc/article/details/52402931}
 */
public class BaseObject {
	// 记录从池中取出次数
	private int num;
	private boolean active;

	public BaseObject() {
		active = true;
		System.out.println("new BaseObject!!!!!");
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
