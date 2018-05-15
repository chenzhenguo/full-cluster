package com.hhcf.system.listner;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

/**
 * @Title: ReceiveConfirmTestListener
 * @Description:RabbitMQ(四)消息确认(发送确认,接收确认)
 * @Author: zhaotf
 * @Since:2018年5月15日 下午3:29:59
 * @see {@linkplain https://blog.csdn.net/qq315737546/article/details/54176560}
 */
public class ReceiveConfirmTestListener implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		// TODO Auto-generated method stub
		try {
			System.out.println("consumer--:" + message.getMessageProperties() + ":" + new String(message.getBody()));
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			e.printStackTrace();// TODO 业务处理
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
		}
	}

}
