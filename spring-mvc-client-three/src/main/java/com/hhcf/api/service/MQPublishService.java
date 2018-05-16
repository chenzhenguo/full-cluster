package com.hhcf.api.service;

/**
 * @Title: MQPublishService
 * @Description:
 * @Author: zhaotf
 * @Since:2018年5月16日 下午1:33:12
 * @see {@linkplain https://blog.csdn.net/qq315737546/article/details/54176560}
 */
public interface MQPublishService {

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param message
	 *            void
	 */
	public void send(String exchange, String routingKey, Object message);

}
