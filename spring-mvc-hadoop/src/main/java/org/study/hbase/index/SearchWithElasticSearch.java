package org.study.hbase.index;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * @Title: SearchWithElasticSearch.java
 * @Description: TODO 使用elasticsearch提高hbase基于列的查询效率
 * @author zhaotf
 * @date 2018年3月4日 下午3:01:05
 * @see {@linkplain http://blog.csdn.net/blacklau/article/details/39781803}
 */
public class SearchWithElasticSearch {

	public static void main(String[] args) throws Exception {
		// elasticsearch相关设置
		Settings settings = Settings.builder()
				// 指定集群名称
				.put("cluster.name", "elasticsearch").put("client.transport.sniff", true).build();
		// 创建客户端，
		@SuppressWarnings("resource")
		Client client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.233.159"), 9300));

		// elasticsearch查询，查询NAME为blacklau的记录
		SearchResponse response = client.prepareSearch("hbase").setTypes("netflow")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				// NAME对应hbase表的列名
				.setPostFilter(new SpanTermQueryBuilder("NAME", "blacklau")).execute().actionGet();
		SearchHits shs = response.getHits();
		// 根据查询到的rowkeys构建Get
		List<Get> gets = new ArrayList<Get>();
		for (SearchHit hit : shs) {
			// ROWKEY对应hbase表的rowkey
			String rowkey = (String) hit.getSourceAsMap().get("ROWKEY");
			Get get = new Get(Bytes.toBytes(rowkey));
			gets.add(get);
		}

		if (gets.size() == 0)
			return;

		HTable table = new HTable(HBaseConfiguration.create(), "netflow");
		Result[] rs = table.get(gets);
		// 打印hbase表查询结果
		for (Result r : rs) {
			System.out.println(r);
		}

		client.close();
		table.close();
	}

}
