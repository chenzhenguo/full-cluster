package com.hhcf.backend.service.impl;

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
		// 交换机key
		String exchange_key = "abc";
		// 队列key
		String queue_key = "test_mq";
		// 内容
		Object object = "aaaaa";
		rbmqProducerService.sendQueue(exchange_key, queue_key, object);
	}

}
