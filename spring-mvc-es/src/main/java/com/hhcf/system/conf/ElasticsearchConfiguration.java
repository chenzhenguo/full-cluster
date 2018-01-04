package com.hhcf.system.conf;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @Title: ElasticsearchConfiguration.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午12:17:16
 * @see {@linkplain http://blog.csdn.net/u013510614/article/details/50838997}
 */
@Configuration
public class ElasticsearchConfiguration implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {
	private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfiguration.class);

//	@Value("${spring.data.elasticsearch.cluster-nodes}")
//	private String clusterNodes;

	private TransportClient transportClient;
	private PreBuiltTransportClient preBuiltTransportClient;

	@Override
	public void destroy() throws Exception {
		try {
			logger.info("Closing elasticSearch client");
			if (transportClient != null) {
				transportClient.close();
			}
		} catch (final Exception e) {
			logger.error("Error closing ElasticSearch client: ", e);
		}
	}

	@Override
	public TransportClient getObject() throws Exception {
		return transportClient;
	}

	@Override
	public Class<TransportClient> getObjectType() {
		return TransportClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		buildClient();
	}

	protected void buildClient() {
		try {
			preBuiltTransportClient = new PreBuiltTransportClient(settings());

//			String InetSocket[] = clusterNodes.split(":");
//			String address = InetSocket[0];
//			Integer port = Integer.valueOf(InetSocket[1]);
			transportClient = preBuiltTransportClient
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 初始化默认的client
	 */
	private Settings settings() {
		// Settings settings = Settings.builder().put("cluster.name",
		// clusterName).put("client.transport.sniff", true).build();
		Settings settings = Settings.builder().put("cluster.name", "ztf-application").build();
		return settings;
	}
}
