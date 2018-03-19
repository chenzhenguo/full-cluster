package org.study.rpc;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.study.rpc.client.RpcProxy;
import org.study.rpc.sample.service.HelloService;

import com.alibaba.fastjson.JSON;

/**
 * @Title: HelloServiceTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午2:32:03
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mvc-rpc.xml")
public class HelloServiceTest {
	@Autowired
	private RpcProxy rpcProxy;

	@Test
	public void helloTest() {
		HelloService helloService = rpcProxy.create(HelloService.class);
		System.out.println("rpc-客户端HelloServiceTest:111111");
		Map<String, Object> result = helloService.hello("World");
		System.out.println("rpc-客户端HelloServiceTest:22222");
		System.out.println("rpc-客户端HelloServiceTest:" + JSON.toJSONString(result));
		// Assert.assertEquals("Hello! World", result);
	}

}
