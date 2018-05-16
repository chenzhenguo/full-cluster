package com.hhcf.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.BaseTest;
import com.hhcf.api.service.MQPublishService;

/**
 * @Title: MQPublishServiceImplTest
 * @Description:
 * @Author: zhaotf
 * @Since:2018年5月16日 下午1:39:00
 * @see {@linkplain https://blog.csdn.net/qq315737546/article/details/54176560}
 */
public class MQPublishServiceImplTest extends BaseTest {
	@Autowired
	private MQPublishService mQPublishService;

	@Test
	public void send() {
		String exchange = "product-topic";
		String routingKey = "com.mj.test.key";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "zhaotf01");
		map.put("age", 333);
		map.put("name", "有要有");

		byte[] ba = map.toString().getBytes();
//		mQPublishService.send(exchange, routingKey, message);
		mQPublishService.send(exchange, routingKey, map.toString());
	}

	@Test
	public void sendConfirm() {
		String exchange = "product-topic";
		String routingKey = "com.mj.ztf.key";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "zhaotf01");
		map.put("age", 333);
		map.put("name", "有要有");
		mQPublishService.send(exchange, routingKey, map.toString());
	}
	
	
	@Test
	public void sendReturn() {
		String exchange = "product-topic";
		String routingKey = "aaaa";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "zhaotf01");
		map.put("age", 333);
		map.put("name", "有要有");

		byte[] ba = map.toString().getBytes();
		Message message = new Message(ba, null);
//		mQPublishService.send(exchange, routingKey, message);
		mQPublishService.send(exchange, routingKey, map.toString());		
	}
}
