package org.study.mq.rabbitmq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.SerializationUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @Title: QueueConsumer
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月4日 上午7:39:48
 * @see {@linkplain https://blog.csdn.net/younger_z/article/details/53243990}
 */
public class QueueConsumer extends EndPoint implements Runnable, Consumer {

	public QueueConsumer(String endpointName) throws IOException, TimeoutException {
		super(endpointName);
	}

	public QueueConsumer(String queue, String exchange, String routingKey) throws Exception {
		super(queue, exchange, routingKey);
	}

	@Override
	public void handleCancel(String arg0) throws IOException {
	}

	@Override
	public void handleCancelOk(String arg0) {
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		System.out.println("rabbitmq消费端Consumer " + consumerTag + " registered");
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
			throws IOException {
		Map map = (HashMap) SerializationUtils.deserialize(body);
		System.out.println(
				"QueueConsumer.handleDelivery," + consumerTag + ",Message Number " + map.toString() + " received.");
	}

	@Override
	public void handleRecoverOk(String arg0) {
	}

	@Override
	public void handleShutdownSignal(String arg0, ShutdownSignalException arg1) {
	}

	@Override
	public void run() {
		try {
			channel.basicConsume(endPointName, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
