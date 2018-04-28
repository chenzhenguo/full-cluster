package com.hhcf.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @Title: RabbitmqListener
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月3日 下午3:38:22
 * @see {@linkplain https://www.cnblogs.com/libra0920/p/6230421.html}
 */
public class RabbitmqListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("消息消费者 onMessage=" + message.toString());
	}

	public void projectAdd(Message message) {
		System.out.println("消息消费者  projectAdd=" + message.toString());
	}

}
