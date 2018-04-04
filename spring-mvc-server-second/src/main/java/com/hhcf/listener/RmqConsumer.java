package com.hhcf.listener;

import com.alibaba.fastjson.JSON;

/**
 * @Title: RmqConsumer
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月4日 下午5:17:56
 * @see {@linkplain https://blog.csdn.net/congcong68/article/details/39693115}
 */
public class RmqConsumer {

	public void rmqProducerMessage(Object object) {
		System.out.println("rabbitmq消息监听,RmqConsumer:" + JSON.toJSONString(object));
	}

}
