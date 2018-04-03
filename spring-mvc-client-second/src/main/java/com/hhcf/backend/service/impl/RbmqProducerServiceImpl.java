package com.hhcf.backend.service.impl;

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
 */
@Service("rbmqProducerService")
@Transactional
public class RbmqProducerServiceImpl implements RbmqProducerService {

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public void sendQueue(String exchange_key, String queue_key, Object object) {
		// convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
		amqpTemplate.convertAndSend(exchange_key, queue_key, object);
	}
	

}
