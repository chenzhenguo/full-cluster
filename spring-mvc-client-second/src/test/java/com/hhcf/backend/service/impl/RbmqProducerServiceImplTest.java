package com.hhcf.backend.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.LocalBaseTest;
import com.hhcf.backend.service.RbmqProducerService;

/**
 * @Title: RbmqProducerServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月3日 下午4:09:07
 * @see {@linkplain https://www.cnblogs.com/libra0920/p/6230421.html}
 */
public class RbmqProducerServiceImplTest extends LocalBaseTest {
	@Resource
	private RbmqProducerService rbmqProducerService;

	/**
	 * 向rabbitmq发送消息
	 */
	@Test
	public void sendQueue() {
		String exchange_key = "hhcf"; // 交换机key
		String queue_key = "hmlcq"; // 队列key
		// 内容
		String object = "aaaaa";
		System.out.println("aaaaaaaaaaaaaaaaaa");
		for (int i = 0; i < 100; i++) {
			rbmqProducerService.sendQueue(exchange_key, queue_key, object + i);
		}
	}

	/**
	 * 向rabbitmq发送消息,topic模式
	 */
	@Test
	public void sendTopicQueue() {
		String exchangeKey = "ztf-hh"; // 交换机key
		String routingKey = "hmlc.msg"; // 路由键
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < 100; i++) {
			map.put("code", System.currentTimeMillis());
			map.put("name", "topic" + i);
			rbmqProducerService.sendTopicQueue(exchangeKey, routingKey, map);
		}
	}

}
