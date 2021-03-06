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
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceRangeQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
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
 * @Description:TODO ES 5.1.1版本
 * @Author: zhaotf
 * @Since:2018年1月2日 下午3:44:11
 * @see {@linkplain http://blog.csdn.net/luosai19910103/article/details/53729783}
 * @see {@linkplain https://www.cnblogs.com/wenbronk/p/6524337.html}
 */
public class ESGeoTransportClientUtils extends BaseJunitTest {
	private String indexName = "local-map-attractions-1";
	private String indexType = "maps";

	@Test
	@Deprecated
	public void testGeoShapeQuery1() {
		GeoShapeQueryBuilder queryBuilder = QueryBuilders.geoShapeQuery("location", "uPhLkNcPQSmVuoJDkMUCkQ", indexType)
				// .geoShapeQuery(
				// "model.location", // field
				// "AVqxrMyikOe4Yke4p_Wx", // id of document
				// "catchModel", ShapeRelation.WITHIN) // type, relation
				.indexedShapeIndex(indexName) // name of index
				.indexedShapePath("location"); // filed specified as path
		SearchResponse response = transportClient.prepareSearch(indexName).setTypes(indexType).setQuery(queryBuilder)
				.execute().actionGet();
		String string = response.getHits().getHits().toString();
		System.out.println(string);
	}

	/**
	 * 使用 BoundingBoxQuery进行查询,落入指定的矩形，左上点/右下点
	 */
	@Test
	public void testGeoBoundingBoxQuery() {
		GeoBoundingBoxQueryBuilder queryBuilder = QueryBuilders.geoBoundingBoxQuery("location")
				.setCorners(new GeoPoint(40.059552, 116.177752), new GeoPoint(39.791302, 116.693451));
		SearchResponse searchResponse = transportClient.prepareSearch(indexName).setTypes(indexType)
				.setQuery(queryBuilder).get();
		System.out.println(searchResponse);
		System.err.println(searchResponse.getHits().totalHits());
	}

	/**
	 * distance query 查询
	 */
	@Test
	public void testDistanceQuery() {
		double lat = 40.812679;// 纬度latitude
		double lon = 117.278338;// 经度longitude
		SearchRequestBuilder srb = transportClient.prepareSearch(indexName).setTypes(indexType).setFrom(0).setSize(10);
		GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("location").point(lat, lon)
				.distance(20, DistanceUnit.KILOMETERS).geoDistance(GeoDistance.ARC);
		// 距离计算用
		srb.setPostFilter(queryBuilder).addSort(
				SortBuilders.geoDistanceSort("location", lat, lon).unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC));

		SearchResponse response = srb.execute().actionGet();
		System.out.println(response.getHits().totalHits() + "," + response);
		SearchHit[] hits = response.getHits().getHits();
		for (SearchHit hit : hits) {

			// 获取距离值，并保留两位小数点
			BigDecimal geoDis = new BigDecimal((Double) hit.getSortValues()[0]);
			Map<String, Object> hitMap = hit.getSource();
			hitMap.put("geoDistance", geoDis.setScale(2, BigDecimal.ROUND_HALF_DOWN));

			System.out.println("附近的人:" + hit.getSourceAsString() + ",距离:" + hit.getSource().get("geoDistance")
					+ DistanceUnit.KILOMETERS.toString());
		}
	}

	/**
	 * 获取附近的人, geo_distance 查询 发现文档geo-points内指定的中心点的距离。
	 */
	@Test
	public void testGetNearbyPeople() {
		double lat = 40.812679;// 纬度latitude
		double lon = 117.278338;// 经度longitude
		SearchRequestBuilder srb = this.transportClient.prepareSearch(indexName).setTypes(indexType);
		srb.setFrom(0).setSize(10);// 1000人
		// lon, lat位于谦的坐标，查询距离于谦1米到1000米
		GeoDistanceQueryBuilder location1 = QueryBuilders.geoDistanceQuery("location").point(lat, lon)
				.distance(110, DistanceUnit.METERS).geoDistance(GeoDistance.ARC);
		// 获取距离多少公里 这个才是获取点与点之间的距离的
		GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("location", lat, lon);
		srb.setPostFilter(location1).addSort(sort.unit(DistanceUnit.METERS).order(SortOrder.ASC).point(lat, lon));

		SearchResponse searchResponse = srb.execute().actionGet();

		Float usetime = searchResponse.getTook().getMillis() / 1000f; // 搜索耗时
		SearchHits hits = searchResponse.getHits();
		System.out.println("于谦附近的人(" + hits.getTotalHits() + "个)，耗时(" + usetime + "秒)：");
		SearchHit[] searchHists = hits.getHits();
		for (SearchHit hit : searchHists) {
			String name = (String) hit.getSource().get("name");
			List<Double> location = (List<Double>) hit.getSource().get("location");
			// 获取距离值，并保留两位小数点
			BigDecimal geoDis = new BigDecimal((Double) hit.getSortValues()[0]);
			Map<String, Object> hitMap = hit.getSource();
			// 在创建MAPPING的时候，属性名的不可为geoDistance。
			hitMap.put("geoDistance", geoDis.setScale(0, BigDecimal.ROUND_HALF_DOWN));
			System.out.println(name + "的坐标：" + location + "他距离于谦" + hit.getSource().get("geoDistance")
					+ DistanceUnit.METERS.toString());
		}
	}

	/**
	 * 环形查询
	 */
	@Deprecated
	@Test
	public void testDistanceRangeQuery() {
		double lat = 40.812679;// 纬度latitude
		double lon = 117.278338;// 经度longitude
		GeoDistanceRangeQueryBuilder gdrqb = QueryBuilders.geoDistanceRangeQuery("location", lat, lon).from("20km")// 内环
				.to("25km")// 外环
				.includeLower(true)// 包含上届
				.includeUpper(true) // 包含下届
				.geoDistance(GeoDistance.SLOPPY_ARC);
		SearchResponse response = transportClient.prepareSearch(indexName).setTypes(indexType)
				// .setSearchType(SearchType.DFS_QUERY_AND_FETCH)
				.setQuery(gdrqb).execute().actionGet();
		System.out.println(response);
		System.out.println(response.getHits().totalHits());
	}

	/**
	 * 多边形查询
	 */
	@Test
	public void testPolygonQuery() {
		List<GeoPoint> points = new ArrayList<>();
		points.add(new GeoPoint(40.057785, 116.391621));
		points.add(new GeoPoint(39.867547, 116.272613));
		points.add(new GeoPoint(39.852925, 116.517527));
		GeoPolygonQueryBuilder queryBuilder = QueryBuilders.geoPolygonQuery("location", points);
		SearchResponse response = transportClient.prepareSearch(indexName).setTypes(indexType).setQuery(queryBuilder)
				.setSize(10).get();
		System.out.println(response);
		System.out.println(response.getHits().totalHits());
	}

	// 添加数据
	@Test
	public void addIndexData100000() throws UnknownHostException {
		List<String> cityList = new ArrayList<String>();
		double lat = 39.929986;// 纬度latitude
		double lon = 116.395645;// 经度longitude
		for (int i = 102000; i < 105000; i++) {
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
			// IndexRequestBuilder builder =
			// transportClient.prepareIndex(indexName, indexType);
			requests.add(transportClient.prepareIndex(indexName, indexType).setSource(text).request());
			System.out.println("aa:" + cityList.indexOf(text) + "," + text);
		}

		// 批量创建索引
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
		for (IndexRequest request : requests) {
			bulkRequest.add(request);
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			System.err.println("批量创建索引错误！");
		}
		System.out.println("批量创建索引数量:" + bulkRequest.numberOfActions());
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
				.startObject("@timestamp").field("type", "date").endObject() // 日间戳
				.startObject("id").field("type", "long").endObject() // 姓名
				.startObject("name").field("type", "text").endObject() // 位置
				.startObject("location").field("type", "geo_point").endObject().endObject().endObject().endObject();
		return mapping;
	}

}
