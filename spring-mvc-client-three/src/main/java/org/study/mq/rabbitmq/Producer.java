package org.study.mq.rabbitmq;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

/**
 * @Title: Producer
 * @Description: 功能概要：消息生产者
 * @Author: zhaotf
 * @Since:2018年4月4日 上午7:37:40
 * @see {@linkplain https://blog.csdn.net/younger_z/article/details/53243990}
 */
public class Producer extends EndPoint {

	public Producer(String endpointName) throws IOException, TimeoutException {
		super(endpointName);
	}

	public Producer(String queue, String exchange, String routingKey) throws Exception {
		super(queue, exchange, routingKey);
	}

	/**
	 * direct
	 * 
	 * @param object
	 *            消息内容
	 * @throws IOException
	 */
	public void sendMessage(Serializable object) throws IOException {
		channel.basicPublish("", endPointName, null, SerializationUtils.serialize(object));
	}

	/**
	 * topic
	 * 
	 * @param routingKey
	 *            路由键
	 * @param object
	 *            消息内容
	 * @throws IOException
	 */
	public void sendTopicMessage(String routingKey, Serializable object) throws IOException {
		channel.basicPublish(exchangeName, routingKey, null, SerializationUtils.serialize(object));
	}

}
