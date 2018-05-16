package com.hhcf.system.listner;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

/**
 * @Title: ReturnCallBackListener
 * @Description:失败后return回调
 * @Author: zhaotf
 * @Since:2018年5月15日 下午3:41:25
 * @see {@linkplain https://blog.csdn.net/qq315737546/article/details/54176560}
 */
//@Component("returnCallBackListener")
public class ReturnCallBackListener implements ReturnCallback {

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		System.out.println("return--消息回调处理:" + new String(message.getBody()) + ",replyCode:" + replyCode
				+ ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
	}

}
