package com.hhcf.front.service.impl;

import java.util.Map;

import com.hhcf.front.service.ConsumerService;

/**
 * @Title: ConsumerServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月3日 下午3:46:04
 * @see {@linkplain https://www.cnblogs.com/libra0920/p/6230421.html}
 */
public class ConsumerServiceImpl implements ConsumerService {

	@Override
	public void getMessage(Map<String, Object> message) {
		System.out.println("消费者：" + message);
	}

}
