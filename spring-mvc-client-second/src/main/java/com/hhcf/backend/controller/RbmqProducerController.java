package com.hhcf.backend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hhcf.backend.service.RbmqProducerService;

/**
 * @Title: RbmqProducerController
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月11日 上午9:24:54
 * @see {@linkplain https://www.cnblogs.com/libra0920/p/6230421.html}
 */
@Controller
@RequestMapping("/rbmqProducer")
public class RbmqProducerController {
	@Resource
	private RbmqProducerService rbmqProducerService;

	@RequestMapping("/sendQueue")
	@ResponseBody
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
	@ResponseBody
	@RequestMapping("/sendTopicQueue")
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
