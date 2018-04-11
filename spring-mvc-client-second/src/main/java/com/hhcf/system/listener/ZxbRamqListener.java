package com.hhcf.system.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @Title: ZxbRamqListener
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月11日 上午10:14:29
 * @see {@linkplain https://blog.csdn.net/u011991249/article/details/53200856}
 */
public class ZxbRamqListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("消息消费者consumor:" + message + "");
		String messageBody = new String(message.getBody());
		System.out.println("消息消费者,反序列化consumor:" + messageBody);
	}

	public void onNzwzMessage(Message message) {
		System.out.println("消息消费者consumor-nzwz:" + message + "");
		String messageBody = new String(message.getBody());
		System.out.println("消息消费者,反序列化consumor-nzwz:" + messageBody);
	}
	
}
