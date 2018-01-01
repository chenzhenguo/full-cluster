package com.hhcf.learn.controller;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.transport.TransportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @Title: ESSearchController.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午12:16:10
 * @see {@linkplain http://blog.csdn.net/u013510614/article/details/50838997}
 */
@RestController
@RequestMapping("/eSSearch")
public class ESSearchController {
	@Autowired
	private TransportClient transportClient;

	/**
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/client.do
	 */
	@RequestMapping(value = "/client")
	public TransportResponse client(String index, String type, String articleId) {
		// transportClient.admin().cluster().
		// Requests.putMappingRequest("").type("").source("")u
		// GetResponse response =
		// transportClient.prepareGet("jdbc-sys-log-20171231", "doc",
		// articleId).get();
		TransportResponse response = transportClient.prepareSearch().execute().actionGet();
		return response;
	}

	/**
	 * 测试scroll api * 对大量数据的处理更有效
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/testScrolls.do
	 */
	@RequestMapping(value = "/testScrolls")
	public Object testScrolls(String index, String type, String articleId) {
		QueryBuilder queryBuilder = QueryBuilders.prefixQuery("logcontent.keyword", "修改成功");
		// QueryBuilder queryBuilder = QueryBuilders.termQuery("loglevel", "2");

		// setQuery 不能重复使用，后面的会覆盖前面的
//		SearchResponse response = transportClient.prepareSearch(index)
//				.setScroll(new TimeValue(60000)).setQuery(queryBuilder).setSize(15).get();
		SearchResponse response = transportClient.prepareSearch(index)
				.setScroll(new TimeValue(60000)).setQuery(queryBuilder).setSize(5).execute().actionGet();

		// Map<String,Object> map = new HashMap<String, Object>();
		List list = new ArrayList<>();
		for (SearchHit hit : response.getHits().getHits()) {
			System.out.println("测试scroll api * 对大量数据的处理更有效:" + hit.getSourceAsMap().toString());

			// list.add(hit.getSourceAsMap());
			list.add(hit.getId() + ";" + hit.getSourceAsMap());
		}
		// while (true) {
		// SearchResponse response2 =
		// transportClient.prepareSearchScroll(response.getScrollId())
		// .setScroll(new TimeValue(60000)).execute().actionGet();
		// if (response2.getHits().getHits().length == 0) {
		// System.out.println("oh no=====");
		// break;
		// }
		// }
		return list;
	}

	/**
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/getFullData.do
	 */
	@RequestMapping(value = "/getFullData")
	public TransportResponse getFullData(String index, String type, String articleId) {
		System.out.println("aa:" + articleId);

		IndexResponse response = transportClient.prepareIndex(index, type, articleId).get();
		System.out.println("ee:");
		return response;
	}

	/**
	 * 通过id查询
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/getData.do
	 */
	@RequestMapping(value = "/getData")
	public TransportResponse getData(String index, String type, String articleId) {
		System.out.println("aa:" + articleId);
		// GetResponse response =
		// transportClient.prepareGet("jdbc-sys-log-20171231", "doc",
		// articleId).get();
		// GetResponse response = transportClient.prepareGet(index, type,
		// articleId).get();
		GetResponse response = transportClient.prepareGet(index, type, articleId).execute().actionGet();
		System.out.println("bb:" + response.getFields().toString());
		System.out.println("ee:");
		return response;
	}

	/**
	 * 测试multiSearch
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/getData.do
	 */
	@RequestMapping(value = "/testMultiSearch")
	public TransportResponse testMultiSearch(String index, String type, String articleId) {
		QueryBuilder qb1 = QueryBuilders.queryStringQuery("elasticsearch");
		SearchRequestBuilder requestBuilder1 = transportClient.prepareSearch().setQuery(qb1).setSize(1);

		QueryBuilder qb2 = QueryBuilders.matchQuery("user", "kimchy");
		SearchRequestBuilder requestBuilder2 = transportClient.prepareSearch().setQuery(qb2).setSize(1);

		MultiSearchResponse multiResponse = transportClient.prepareMultiSearch().add(requestBuilder1)
				.add(requestBuilder2).execute().actionGet();
		long nbHits = 0;
		for (MultiSearchResponse.Item item : multiResponse.getResponses()) {
			SearchResponse response = item.getResponse();
			nbHits = response.getHits().getTotalHits();
			SearchHit[] hits = response.getHits().getHits();
			System.out.println(nbHits);
		}
		return multiResponse;
	}

	/**
	 * 测试聚合查询
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/getData.do
	 */
	@RequestMapping(value = "/testAggregation")
	public TransportResponse testAggregation() {
		SearchResponse response = transportClient.prepareSearch().setQuery(QueryBuilders.matchAllQuery()) // 先使用query过滤掉一部分
				.addAggregation(AggregationBuilders.terms("term").field("user")).execute().actionGet();
		// .addAggregation(AggregationBuilders.dateHistogram("agg2").field("birth").interval()).execute()
		// Aggregation aggregation2 = response.getAggregations().get("term");
		// Aggregation aggregation = response.getAggregations().get("agg2");
		// SearchResponse response2 = client.search(new
		// SearchRequest().searchType(SearchType.QUERY_AND_FETCH)).actionGet();
		return response;
	}

	/**
	 * 过滤查询: 大于gt, 小于lt, 小于等于lte, 大于等于gte
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/testFilter.do
	 */
	@RequestMapping(value = "/testFilter")
	public TransportResponse testFilter(String index, String type, String articleId) {
		SearchResponse response = transportClient.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.matchAllQuery()) // 查询所有
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				// .setPostFilter(FilterBuilders.rangeFilter("age").from(0).to(19)
				// .includeLower(true).includeUpper(true))
				// .setPostFilter(FilterBuilderFactory
				// .rangeFilter("age").gte(18).lte(22))
				.setExplain(true) // explain为true表示根据数据相关度排序，和关键字匹配最高的排在前面
				.get();
		return response;
	}

	/**
	 * 分组查询
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/testGroupBy.do
	 */
	@RequestMapping(value = "/testGroupBy")
	public Object testGroupBy(String index, String type, String articleId) {
		SearchResponse response = transportClient.prepareSearch(index).setTypes(type)
				.setQuery(QueryBuilders.matchAllQuery()).setSearchType(SearchType.QUERY_THEN_FETCH)
				.addAggregation(AggregationBuilders.terms("loglevel").field("loglevel").size(10)).get();
		// 根据user进行分组，size(0)，也是10
		SearchHits hits = response.getHits();
		System.out.println("分组查询,总记录数:" + hits.getTotalHits());
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit sh : searchHits) {
			System.out.println(sh.getSourceAsString());
		}
		return null;
	}

}
