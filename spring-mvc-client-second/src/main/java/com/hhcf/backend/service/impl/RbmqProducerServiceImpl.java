package com.hhcf.backend.service.impl;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.backend.service.RbmqProducerService;

/**
 * @Title: RbmqProducerServiceImpl
 * @Description:消息队列发送者
 * @Author: zhaotf
 * @Since:2018年4月3日 上午11:23:45
 * @see {@linkplain https://www.cnblogs.com/libra0920/p/6230421.html}
 * @see {@linkplain https://www.cnblogs.com/ityouknow/p/6120544.html}
 */
@Service("rbmqProducerService")
@Transactional
public class RbmqProducerServiceImpl implements RbmqProducerService {

	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private AmqpTemplate ztfAmqpTemplate;

	@Override
	public void sendQueue(String exchange_key, String queue_key, Object object) {
		// convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
		System.out.println("cccccccccccccc");
		// amqpTemplate.convertAndSend(exchange_key, queue_key, object);
		amqpTemplate.convertAndSend(exchange_key, "hmlc.v1.ztf", object);

		// Topic Exchange 模式
		// amqpTemplate.convertAndSend("testTopicExchange", "key1.a.c.key2",
		// "msg-消息内容");
	}

	@Override
	public void sendTopicQueue(String exchangeKey, String routingKey, Object object) {
		System.out.println("aaaaaaaaaa");
		// convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
		ztfAmqpTemplate.convertAndSend(exchangeKey, routingKey, object);
	}

	/**
	 * 接收消息
	 * 
	 * @return Object
	 */
	public Object test() {
		this.amqpTemplate.receiveAndConvert("", 3l);
		return null;
	}

}
