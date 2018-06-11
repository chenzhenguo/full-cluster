package com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Title: DubboProvider
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月11日 上午11:14:40
 * @see {@linkplain https://blog.csdn.net/itlqi/article/details/77973266}
 */
public class DubboProvider {
	private static final Log log = LogFactory.getLog(DubboProvider.class);

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-mvc-job.xml",
					"classpath:spring-mvc-mybaits.xml");
			context.start();
		} catch (Exception e) {
			log.error("== DubboProvider context start error:", e);
		}
		synchronized (DubboProvider.class) {
			while (true) {
				try {
					DubboProvider.class.wait();
				} catch (InterruptedException e) {
					log.error("== synchronized error:", e);
				}
			}
		}
	}

}
