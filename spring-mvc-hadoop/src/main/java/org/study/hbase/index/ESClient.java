package org.study.hbase.index;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @Title: ESClient.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月4日 下午12:57:46
 * @see {@linkplain http://blog.csdn.net/fxsdbt520/article/details/53884338}
 */
public class ESClient {

	// ElasticSearch的集群名称
	public static String clusterName;
	// ElasticSearch的host
	public static String nodeHost;
	// ElasticSearch的端口（Java API用的是Transport端口，也就是TCP）
	public static int nodePort;
	// ElasticSearch的索引名称
	public static String indexName;
	// ElasticSearch的类型名称
	public static String typeName;
	// ElasticSearch Client
	public static Client client;

	/**
	 * get Es config
	 *
	 * @return
	 */
	public static String getInfo() {
		List<String> fields = new ArrayList<String>();
		try {
			for (Field f : ESClient.class.getDeclaredFields()) {
				fields.add(f.getName() + "=" + f.get(null));
			}
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return StringUtils.join(fields, ", ");
	}

	/**
	 * init ES client
	 */
	@SuppressWarnings("resource")
	public static void initEsClient() throws Exception {
		Settings settings = Settings.builder().put("cluster.name", ESClient.clusterName).build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(ESClient.nodeHost), ESClient.nodePort));
	}

	/**
	 * Close ES client
	 */
	public static void closeEsClient() {
		client.close();
	}
}
