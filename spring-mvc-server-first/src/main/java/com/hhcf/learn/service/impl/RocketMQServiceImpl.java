package com.hhcf.learn.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.hhcf.learn.service.RocketMQService;

/**
 * 
 * @Title: RocketMQServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月12日 下午4:56:35
 */
@Service
public class RocketMQServiceImpl implements RocketMQService {
	@Autowired
	private DefaultMQProducer defaultMQProducer;

	@Override
	public Object sendMessage(HttpServletRequest request) throws Exception {
		String topic = request.getParameter("topic");
		String tags = request.getParameter("tags");
		String keys = request.getParameter("keys");
		String content = request.getParameter("content");
		Message msg = new Message(topic, tags, keys, (content + System.currentTimeMillis()).getBytes());
		SendResult sendResult = defaultMQProducer.send(msg);
		System.out.println(sendResult);
		return sendResult;
	}

}
