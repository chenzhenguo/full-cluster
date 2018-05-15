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
 * @see {@linkplain https://www.cnblogs.com/wuzhiyuan/p/6862036.html}
 */
public class ReceiveConfirmTestListener implements ChannelAwareMessageListener {

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		try {
			System.out.println("consumer--:" + message.getMessageProperties() + ":" + new String(message.getBody()));
			// deliveryTag是消息传送的次数，我这里是为了让消息队列的第一个消息到达的时候抛出异常，处理异常让消息重新回到队列，然后再次抛出异常，处理异常拒绝让消息重回队列
			if (message.getMessageProperties().getDeliveryTag() == 1
					|| message.getMessageProperties().getDeliveryTag() == 2) {
				throw new Exception();
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			e.printStackTrace();// TODO 业务处理
//			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			if (message.getMessageProperties().getRedelivered()) {
				System.out.println("消息已重复处理失败,拒绝再次接收...");
				channel.basicReject(message.getMessageProperties().getDeliveryTag(), true); // 拒绝消息
			} else {
				System.out.println("消息即将再次返回队列处理...");
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true); // requeue为是否重新回到队列
			}

		}
	}

}
