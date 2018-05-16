package com.hhcf.api.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.api.service.MQPublishService;

/**
 * @see {@linkplain https://blog.csdn.net/qq315737546/article/details/54176560}
 * @Title: MQPublishServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年5月16日 下午1:34:14
 */
@Service("mQPublishService")
@Transactional
public class MQPublishServiceImpl implements MQPublishService {
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public void send(String exchange, String routingKey, Object message) {
		amqpTemplate.convertAndSend(exchange, routingKey, message);
	}

}
