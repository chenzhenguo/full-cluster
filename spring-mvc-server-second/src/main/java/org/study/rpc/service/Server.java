package org.study.rpc.service;

/**
 * @Title: Server.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午4:33:01
 * @see {@linkplain http://blog.csdn.net/rulon147/article/details/53814589}
 */
public interface Server {

	public void stop();

	public void start() throws Exception;

	public void register(Class serviceInterface, Class impl);

	public boolean isRunning();

	public int getPort();

}
