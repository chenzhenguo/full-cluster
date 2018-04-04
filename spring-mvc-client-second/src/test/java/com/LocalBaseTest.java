package com;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @Title: LocalBaseTest
 * @Description:测试基类
 * @Author: zhaotf
 * @Since:2018年4月3日 下午4:10:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath*:spring-mvc.xml", "classpath*:spring-mvc-hibernate.xml", "classpath*:spring-mvc-rabbitmq.xml" })
@Transactional
public class LocalBaseTest {
	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;
	@Autowired
	protected WebApplicationContext wac;

	@Before
	public void init() {
		MockHttpServletRequestBuilder requestBuilder = post("/");
		request = requestBuilder.buildRequest(this.wac.getServletContext());
		response = new MockHttpServletResponse();
	}

	@Test
	public void method1() {
		System.out.println("aaaaaaaaa");
	}

}
