package org.study.rpc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.study.rpc.client.RpcProxy;

/**
 * @Title: HelloServiceTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午2:31:01
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class HelloService {

	public String hello(String string) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Autowired
//	private RpcProxy rpcProxy;
//
//	@Test
//	public void helloTest() {
//		HelloService helloService = rpcProxy.create(HelloService.class);
//		String result = helloService.hello("World");
//		System.out.println("rpc客户端:" + result);
//		// Assert.assertEquals("Hello! World", result);
//	}

}
