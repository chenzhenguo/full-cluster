package com.hhcf.system.util;

import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Before;
import org.junit.Test;

import com.BaseJunitTest;
import com.google.gson.GsonBuilder;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.indices.mapping.GetMapping;

/**
 * 
 * @Title: ESSearchJestUtilsTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年1月1日 下午8:58:09
 * @see {@linkplain http://blog.csdn.net/vinegar93/article/details/53223819}
 */
public class ESSearchJestUtilsTest extends BaseJunitTest {
	private JestClient jestClient;

	/**
	 * 获取JestClient对象
	 */
	@Before
	public void initJestClient() {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder("http://localhost:9200")
				.gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create()).connTimeout(1500)
				.readTimeout(3000).multiThreaded(true).build());
		// return factory.getObject();
		jestClient = factory.getObject();
	}

	@Test
	public void createIndex() {
		System.out.println("aaaaaaaaaaaa");
	}

	/**
	 * Get映射
	 */
	@Test
	public void getIndexMapping() throws Exception {
		GetMapping getMapping = new GetMapping.Builder().addIndex("jdbc-sys-log-20171231").addType("doc").build();
		JestResult jr = jestClient.execute(getMapping);
		System.out.println("Get映射:" + jr.getJsonString());
	}

	/**
	 * 索引文档,错误，检索不到数据
	 */
	@Test
	public void index() throws Exception {
		Bulk.Builder bulk = new Bulk.Builder().defaultIndex("jdbc-sys-log-20171231").defaultType("doc");
		// for (Object obj : objs) {
		// Index index = new Index.Builder(obj).build();
		// bulk.addAction(index);
		// }
		Index index = new Index.Builder("").id("ZpHIrGABAQ-l7kjeZlC4").build();
		bulk.addAction(index);
		BulkResult br = jestClient.execute(bulk.build());
		// return br.isSucceeded();
		System.out.println("索引文档:" + br.getJsonString());
	}

	/**
	 * 搜索文档
	 */
	@Test
	public void search() throws Exception {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// query不能使用多个，后面的后覆盖前面的
		searchSourceBuilder.query(QueryBuilders.termQuery("logcontent.keyword", "修改成功"));
		// searchSourceBuilder.query(QueryBuilders.termQuery("loglevel", "1"));
		System.out.println("searchSourceBuilder:" + searchSourceBuilder.toString());

		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("jdbc-sys-log-20171231")
				.addType("doc").build();
		SearchResult rslt = jestClient.execute(search);
		System.out.println("索引文档:" + rslt.getJsonString());

		List<String> list = rslt.getSourceAsStringList();
		for (String text : list) {
			System.out.println("索引文档列表:" + text);
		}

	}

	/**
	 * 搜索高亮显示
	 * 
	 * @throws Exception
	 */
	@Test
	public void createSearch() throws Exception {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("loglevel", "1"));
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("logcontent");// 高亮title
		highlightBuilder.preTags("<em>").postTags("</em>");// 高亮标签
		highlightBuilder.fragmentSize(500);// 高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);

		System.out.println("searchSourceBuilder:" + searchSourceBuilder.toString());

		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("jdbc-sys-log-20171231")
				.addType("doc").build();

		SearchResult result = jestClient.execute(search);
		System.out.println(result.getJsonString());
		System.out.println("本次查询共查到：" + result.getTotal() + "篇文章！");
		List<Hit<Object, Void>> hits = result.getHits(Object.class);

		System.out.println(hits.size());
		for (Hit<Object, Void> hit : hits) {

			Object source = hit.source;
			System.out.println("数据:" + source.getClass() + "," + source);
			// // 获取高亮后的内容
			// Map<String, List<String>> highlight = hit.highlight;
			//
			// List<String> views = highlight.get("view");// 高亮后的title
			// if (views != null) {
			// source.setView(views.get(0));
			// }
			// System.out.println("标题：" + source.getTitile());
			// System.out.println("内容：" + source.getContent());
			// System.out.println("浏览数：" + source.getView());
			// System.out.println("标签：" + source.getTag());
			// System.out.println("作者：" + source.getAuthor());
		}
	}

}
