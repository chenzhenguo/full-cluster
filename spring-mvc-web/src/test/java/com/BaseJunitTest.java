package com;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @Title: BaseJunitTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午2:55:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml", "classpath:spring-mvc-mybaits.xml" })
public class BaseJunitTest {
	protected TransportClient transportClient;

	@Before
	public void initClient() throws UnknownHostException {
		Settings settings = Settings.builder().put("cluster.name", "my-application").build();
		PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
		transportClient = preBuiltTransportClient
				.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
	}
	// @Test
	// public void testM1() {
	// System.out.println("aaaaaaa");
	// }

}
