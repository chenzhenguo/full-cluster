package org.study.nio.apache;

/**
 * 
 * @Title: MyFutureCallback
 * @Description: 非阻塞回调函数
 * @Author: zhaotf
 * @Since:2018年3月12日 下午2:03:04
 */
public interface MyFutureCallback<T> {
	/**
	 * 成功
	 * 
	 * @param result
	 */
	public void completed(T result);

	/**
	 * 失败
	 * 
	 * @param ex
	 */
	public void failed(Exception ex);

	/**
	 * 请求取消
	 */
	public void cancelled();

}
