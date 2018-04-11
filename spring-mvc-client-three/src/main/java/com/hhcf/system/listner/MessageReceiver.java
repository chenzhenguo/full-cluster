package com.hhcf.system.listner;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @Title: MessageReceiver
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月11日 下午1:57:32
 * @see {@linkplain https://www.cnblogs.com/jun-ma/p/4864243.html}
 */
public class MessageReceiver implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("Consumer侦听mq消息代码:" + message);
	}

}
