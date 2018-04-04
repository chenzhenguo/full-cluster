package com.hhcf.backend.service;

/**
 * @Title: RbmqProducerService
 * @Description:
 * @Author: zhaotf
 * @Since:2018年4月3日 上午11:23:18
 * @see {@linkplain https://www.cnblogs.com/libra0920/p/6230421.html}
 */
public interface RbmqProducerService {

	/**
	 * 向rabbitmq发送消息,dicret模式
	 * 
	 * @param exchange_key
	 *            交换机key
	 * @param queue_key
	 *            队列key
	 * @param object
	 *            内容
	 */
	public void sendQueue(String exchange_key, String queue_key, Object object);

	/**
	 * 向rabbitmq发送消息,topic模式
	 * 
	 * @param exchangeKey
	 *            交换机key
	 * @param routingKey
	 *            路由键
	 * @param object
	 *            内容
	 */
	public void sendTopicQueue(String exchangeKey, String routingKey, Object object);

}
