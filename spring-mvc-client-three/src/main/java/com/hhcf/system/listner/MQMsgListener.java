package com.hhcf.system.listner;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

/**
 * @Title: MQMsgListener
 * @Description:
 * @Author: zhaotf
 * @Since:2018年5月16日 下午2:41:59
 * @see {@linkplain https://www.cnblogs.com/piaolingzxh/p/5448927.html}
 */
public class MQMsgListener implements ChannelAwareMessageListener {

	public void receiveMsg(Map<String, Object> message) {
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		System.out.println("MQMsgListener.receiveMsg侦听mq消息代码:" + message);
		try {
			System.out.println("MQMsgListener.onMessage--监听端:" + message.getMessageProperties() + ":"
					+ new String(message.getBody(), "UTF-8"));
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			e.printStackTrace();// TODO 业务处理
			if (message.getMessageProperties().getRedelivered()) {
				System.out.println("消息已重复处理失败,拒绝再次接收...");
				channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); // 拒绝消息,单条
			} else {
				System.out.println("消息即将再次返回队列处理...");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true); // 拒绝消息,可多条，
																									// requeue为是否重新回到队列
			}
		}
	}

}
