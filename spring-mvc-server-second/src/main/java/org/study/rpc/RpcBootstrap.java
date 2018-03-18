package org.study.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Title: RpcBootstrap.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午12:43:05
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcBootstrap {
	
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("spring-mvc-rpc.xml");
	}

}
