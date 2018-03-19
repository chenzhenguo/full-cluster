package org.study.rpc;

import org.study.rpc.sample.service.HelloService;
import org.study.rpc.sample.service.impl.HelloServiceImpl;
import org.study.rpc.service.Server;
import org.study.rpc.service.impl.CenterImpl;

/**
 * @Title: RPCTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午6:17:45
 * @see {@linkplain http://blog.csdn.net/rulon147/article/details/53814589}
 */
public class RPCServerTest {

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable() {
			public void run() {
				try {
					Server serviceServer = new CenterImpl(8080);
					serviceServer.register(HelloService.class, HelloServiceImpl.class);
					serviceServer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
