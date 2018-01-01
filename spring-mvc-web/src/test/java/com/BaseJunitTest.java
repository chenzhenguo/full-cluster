package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @Title: BaseJunitTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午2:55:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mvc.xml", "classpath:spring-mvc-mybaits.xml" })
public class BaseJunitTest {

//	@Test
//	public void testM1() {
//		System.out.println("aaaaaaa");
//	}

}
