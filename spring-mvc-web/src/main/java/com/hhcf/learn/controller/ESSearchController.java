package com.hhcf.learn.controller;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.transport.TransportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping(value = "/client/{articleId}")
	public TransportResponse putDate(String articleId) {
		// transportClient.admin().cluster().
		// Requests.putMappingRequest("").type("").source("")u
		GetResponse response = transportClient.prepareGet("jdbc-sys-log-20171231", "doc", articleId).get();
		return response;
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
	 * @see http://127.0.0.1:8080/spring-mvc-web/eSSearch/getData.do
	 */
	@RequestMapping(value = "/getData")
	public TransportResponse getData(String articleId) {
		System.out.println("aa:" + articleId);
		GetResponse response = transportClient.prepareGet("jdbc-sys-log-20171231", "doc", articleId).get();
		System.out.println("bb:" + response.getFields().toString());
		System.out.println("ee:");
		return response;
	}

}
