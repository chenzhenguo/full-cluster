package org.study.mq.rabbitmq;

import java.util.HashMap;

/**
 * @Title: Test
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月4日 上午7:45:00
 * @see {@linkplain https://blog.csdn.net/younger_z/article/details/53243990}
 */
public class Test {

	public static void main(String[] args) throws Exception {
		// new Test();
		// new Test("hmlc-topci-qu", "hhcf", "hmlc.v1.ztf");
		new Test("hmlcq", "hhcf", "hmlc.v1.ztf");

	}

	/**
	 * direct
	 * 
	 * @throws Exception
	 */
	public Test() throws Exception {
		QueueConsumer consumer = new QueueConsumer("hmlcq");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		Producer producer = new Producer("hmlcq");

		HashMap message = new HashMap();
		for (int i = 0; i < 100000; i++) {
			message.put("message number", i);
			producer.sendMessage(message);
			System.out.println("direct模式-Message Number " + i + " sent.");
		}
		producer.close();// 消费端关闭
		consumer.close();// 消费端关闭
	}

	/**
	 * topic
	 * 
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 * @throws Exception
	 */
	public Test(String queue, String exchange, String routingKey) throws Exception {
		QueueConsumer consumer = new QueueConsumer(queue, exchange, routingKey);// 创建消费端
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		Producer producer = new Producer(queue, exchange, routingKey);// 创建生产端

		HashMap<String, Object> message = new HashMap<String, Object>();
		for (int i = 0; i < 1000; i++) {
			message.put(routingKey, "topic,message:" + i);
			producer.sendTopicMessage(routingKey, message);
			System.out.println("topic模式-Message Number " + i + " sent.");
		}

		Thread.sleep(8 * 1000);

		System.out.println("topic模式结束");
		producer.close();// 消费端关闭
		consumer.close();// 消费端关闭
	}

}
