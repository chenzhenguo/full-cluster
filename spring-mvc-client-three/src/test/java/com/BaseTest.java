package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @Title: BaseTest
 * @Description:
 * @Author: zhaotf
 * @Since:2018年5月16日 下午1:39:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml", "classpath:spring-mvc-hibernate.xml",
		"classpath:spring-mvc-rabbitmq.xml" })
public class BaseTest {

	@Test
	public void test1() throws InterruptedException {
		String message = "currentTime:" + System.currentTimeMillis();
		System.out.println("test1---message:" + message);
		// exchange,queue 都正确,confirm被回调, ack=true
		// publishService.send(exChange, "CONFIRM_TEST", message);
		Thread.sleep(1000);
	}

}
