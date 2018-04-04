package org.study.mq.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Title: EndPoint
 * @Description:EndPoint类型的队列
 * @Author: zhaotf
 * @Since:2018年4月4日 上午7:36:02
 * @see {@linkplain https://blog.csdn.net/younger_z/article/details/53243990}
 */
public abstract class EndPoint {

	protected Channel channel;
	protected Connection connection;
	protected String endPointName;// queue
	protected String exchangeName;// 交换机名称

	/**
	 * direct模式
	 * 
	 * @param endpointName
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public EndPoint(String endpointName) throws IOException, TimeoutException {
		this.endPointName = endpointName;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setUsername("zhaotf");
		factory.setPassword("123456");
		factory.setVirtualHost("/");
		factory.setConnectionTimeout(30 * 1000);

		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(endpointName, false, false, false, null);// 声明队列
	}

	/**
	 * topic模式
	 * 
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 *            路由键
	 * @throws Exception
	 */
	public EndPoint(String queue, String exchange, String routingKey) throws Exception {
		this.endPointName = queue;
		this.exchangeName = exchange;
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setUsername("zhaotf");
		factory.setPassword("123456");
		factory.setVirtualHost("/");
		factory.setConnectionTimeout(30 * 1000);

		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(queue, false, false, false, null);// 声明队列
		channel.exchangeDeclare("", BuiltinExchangeType.TOPIC);// 声明交换机
//		channel.exchangeDeclare(exchange, type, durable)
		channel.queueBind(queue, exchange, routingKey);// 绑定交换机,并设定路由键
	}

	/**
	 * 关闭channel和connection。并非必须，因为隐含是自动调用的。
	 * 
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public void close() throws IOException, TimeoutException {
		this.channel.close();
		this.connection.close();
	}

}
