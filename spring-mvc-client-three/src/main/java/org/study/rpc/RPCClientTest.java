package org.study.rpc;

import java.net.InetSocketAddress;

import org.study.rpc.sample.service.HelloService;

/**
 * @Title: RPCTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午6:19:32
 * @see {@linkplain http://blog.csdn.net/rulon147/article/details/53814589}
 */
public class RPCClientTest {

	public static void main(String[] args) {
		HelloService service = RPCClient.getRemoteProxyObj(HelloService.class,
				new InetSocketAddress("localhost", 8080));
		System.out.println(service.sayHi("test"));
		System.out.println(service.hello("test"));
	}
	
}
