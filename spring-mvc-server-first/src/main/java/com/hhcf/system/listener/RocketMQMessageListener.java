package com.hhcf.system.listener;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * 
 * @Title: RocketMQMessageListener
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月12日 上午10:43:19
 * @see {@linkplain http://blog.csdn.net/u013086172/article/details/52814850}
 */
public class RocketMQMessageListener implements MessageListenerConcurrently {

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		for (MessageExt messageExt : msgs) {
			System.out.println("消息队列-RocketMQ:" + messageExt.toString() + "," + (new String(messageExt.getBody())));
		}
		System.out.println("消息队列-RocketMQ:" + "getDelayLevelWhenNextConsume=" + context.getDelayLevelWhenNextConsume()
				+ "getMessageQueue=" + context.getMessageQueue().toString() + "getDelayLevelWhenNextConsume="
				+ context.getDelayLevelWhenNextConsume());
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
