package com.hhcf.system.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import com.BaseJunitTest;
import com.hhcf.learn.model.ESUserModel;

/**
 * 
 * @Title: ESGeoTransportClientUtils
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月2日 下午3:44:11
 * @see {@linkplain http://blog.csdn.net/luosai19910103/article/details/53729783}
 */
public class ESGeoTransportClientUtils extends BaseJunitTest {
	private String indexName = "map-attractions";
	private String indexType = "posi";

	// 获取附近的人
	@Test
	public void testGetNearbyPeople() {
		double lat = 40.812679;// 纬度latitude
		double lon = 117.278338;// 经度longitude
		SearchRequestBuilder srb = this.transportClient.prepareSearch(indexName).setTypes(indexType);
		srb.setFrom(0).setSize(10);// 1000人
		// lon, lat位于谦的坐标，查询距离于谦1米到1000米
		// FilterBuilder builder = geoDistanceRangeFilter("location").point(lon,
		// lat).from("1m").to("100m").optimizeBbox("memory").geoDistance(GeoDistance.PLANE);
		GeoDistanceQueryBuilder location1 = QueryBuilders.geoDistanceQuery("location").point(lat, lon).distance(100,
				DistanceUnit.METERS);
		srb.setPostFilter(location1);
		// 获取距离多少公里 这个才是获取点与点之间的距离的
		GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("location", lat, lon);
		sort.unit(DistanceUnit.METERS);
		sort.order(SortOrder.ASC);
		sort.point(lat, lon);
		srb.addSort(sort);

		SearchResponse searchResponse = srb.execute().actionGet();

		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHists = hits.getHits();
		// 搜索耗时
		Float usetime = searchResponse.getTook().getMillis() / 1000f;
		System.out.println("于谦附近的人(" + hits.getTotalHits() + "个)，耗时(" + usetime + "秒)：");
		for (SearchHit hit : searchHists) {
			String name = (String) hit.getSourceAsMap().get("name");
			List<Double> location = (List<Double>) hit.getSourceAsMap().get("location");
			// 获取距离值，并保留两位小数点
			BigDecimal geoDis = new BigDecimal((Double) hit.getSortValues()[0]);
			Map<String, Object> hitMap = hit.getSourceAsMap();
			// 在创建MAPPING的时候，属性名的不可为geoDistance。
			hitMap.put("geoDistance", geoDis.setScale(0, BigDecimal.ROUND_HALF_DOWN));
			System.out.println(name + "的坐标：" + location + "他距离于谦" + hit.getSourceAsMap().get("geoDistance")
					+ DistanceUnit.METERS.toString());
		}

	}

	// 添加数据
	@Test
	public void addIndexData100000() throws UnknownHostException {
		List<String> cityList = new ArrayList<String>();
		double lat = 39.929986;// 纬度latitude
		double lon = 116.395645;// 经度longitude
		for (int i = 101001; i < 101500; i++) {
			double max = 0.00001;
			double min = 0.000001;
			Random random = new Random();
			double s = random.nextDouble() % (max - min + 1) + max;
			DecimalFormat df = new DecimalFormat("######0.000000");
			String lats = df.format(s + lat);
			String lons = df.format(s + lon);
			Double dlat = Double.valueOf(lats);
			Double dlon = Double.valueOf(lons);

			System.out.println("添加数据:" + s + "," + dlat + "," + dlon);
			ESUserModel city1 = new ESUserModel(Long.valueOf(i), "郭德纲" + i, dlat, dlon);
			cityList.add(obj2JsonUserData(city1));
		}
		// 创建索引库
		List<IndexRequest> requests = new ArrayList<IndexRequest>();
		for (String text : cityList) {
			IndexRequestBuilder builder = transportClient.prepareIndex(indexName, indexType);
			builder = builder.setSource(text, XContentType.JSON);
			requests.add(builder.request());
			System.out.println("aa:" + cityList.indexOf(text) + "," + text);
		}

		// 批量创建索引
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
		for (IndexRequest request : requests) {
			bulkRequest.add(request);
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			System.out.println("批量创建索引错误！");
		}
		// return bulkRequest.numberOfActions();
	}

	public String obj2JsonUserData(ESUserModel user) {
		String jsonData = null;
		try {
			// 使用XContentBuilder创建json数据
			XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
			jsonBuild.startObject().field("id", user.getId()).field("name", user.getName()).startArray("location")
					.value(user.getLon()).value(user.getLat()).endArray().endObject();
			jsonData = jsonBuild.string();
			System.out.println(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonData;
	}

	// 创建索引
	@Test
	public void createIndex() throws Exception {
		String indexName = "map-attractions";
		String indexType = "posi";
		// 创建Mapping
		XContentBuilder mapping = createMapping(indexType);
		System.out.println("mapping:" + mapping.string());
		// 创建一个空索引
		transportClient.admin().indices().prepareCreate(indexName).execute().actionGet();
		PutMappingRequest putMapping = Requests.putMappingRequest(indexName).type(indexType).source(mapping);
		PutMappingResponse response = transportClient.admin().indices().putMapping(putMapping).actionGet();
		if (!response.isAcknowledged()) {
			System.out.println("创建失败，Could not define mapping for type [" + indexName + "]/[" + indexType + "].");
		} else {
			System.out.println(
					"创建成功，Mapping definition for [" + indexName + "]/[" + indexType + "] succesfully created.");
		}
	}

	// 创建mapping
	public XContentBuilder createMapping(String indexType) throws Exception {
		XContentBuilder mapping = XContentFactory.jsonBuilder().startObject() // 索引库名（类似数据库中的表）
				.startObject(indexType).startObject("properties")// ID
				.startObject("id").field("type", "long").endObject() // 姓名
				.startObject("name").field("type", "text").endObject() // 位置
				.startObject("location").field("type", "geo_point").endObject().endObject().endObject().endObject();
		return mapping;
	}

}
