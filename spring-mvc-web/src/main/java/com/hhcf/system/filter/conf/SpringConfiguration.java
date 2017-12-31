package com.hhcf.system.filter.conf;

import java.util.LinkedHashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.ClientConfig;

/**
 * 
 * @Title: SpringConfiguration.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午12:17:16
 * @see {@linkplain http://blog.csdn.net/u013510614/article/details/50838997}
 */
@Configuration
public class SpringConfiguration {
	private String connectionUrl = "http://127.0.0.1:9200";

//	@Bean
//	public ClientConfig clientConfig() {
////		ClientConfig clientConfig = new ClientConfig();
//		ClientConfig clientConfig = ClientConfig.Builder.;
//		LinkedHashSetservers = new LinkedHashSet();
//		servers.add(connectionUrl);
//		clientConfig.getServerProperties().put(ClientConstants.SERVER_LIST, servers);
//		clientConfig.getClientFeatures().put(ClientConstants.IS_MULTI_THREADED, false);
//		return clientConfig;
//	}
//
//	@Bean
//	public JestClient jestClient() {
//		JestClientFactory factory = new JestClientFactory();
//		factory.setClientConfig(clientConfig());
//		return factory.getObject();
//	}
}
