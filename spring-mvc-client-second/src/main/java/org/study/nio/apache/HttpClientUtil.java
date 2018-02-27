package org.study.nio.apache;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HttpClientUtil {
	public static void main(String[] args) {
		init();
		System.out.println("aa:" + doGet("http://www.chinaso.com/"));
	}

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static final String CHARSET_UTF8 = StandardCharsets.UTF_8.name();// 字符编码
	private static PoolingHttpClientConnectionManager poolManager;// 连接池管理
	private static RequestConfig requestConfig;// 连接配置

	/**
	 * 初始化连接池，spring容器初始化进行
	 * 
	 */
	// @PostConstruct
	public static void init() {
		try {
			LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", sslsf).register(HttpHost.DEFAULT_SCHEME_NAME, new PlainConnectionSocketFactory())
					.build();
			poolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			poolManager.setMaxTotal(200);// 连接池最大并发连接数 TODO 配置成系统常量
			poolManager.setDefaultMaxPerRoute(poolManager.getMaxTotal());// 单台主机的最大并发连接数
			requestConfig = RequestConfig.custom().setConnectTimeout(1000 * 20)// 表示建立连接的超时时间
					.setSocketTimeout(1000 * 20)// 数据传输处理时间
					.build();
		} catch (NoSuchAlgorithmException e) {
			logger.error("httpclient factory初始化异常", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 提供HttpClient实例
	 * 
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient getHttpClient() {
		return HttpClients.custom().setConnectionManager(poolManager).setDefaultRequestConfig(requestConfig).build();
	}

	/**
	 * http get请求
	 * 
	 * @param url
	 * @return String
	 */
	public static String doGet(String url) {
		CloseableHttpResponse response = null;
		try {
			url = URLDecoder.decode(url, CHARSET_UTF8);
			HttpGet request = new HttpGet(url);
			response = getHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());// 返回json格式
			} else {
				logger.warn("HTTP GET请求失败:" + url + "," + response.getStatusLine().getStatusCode() + ","
						+ response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			logger.error("http get请求异常:" + url, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("http流POST异常", e);
			}
		}
		return null;
	}

	/**
	 * http POST文本流
	 * 
	 * @param url
	 * @param params
	 *            参数文本
	 * @return String
	 */
	public static String doPost(String url, String params) {
		return doPost(url, params, (Header[]) Arrays
				.asList(new BasicHeader("Content-Type", "application/x-www-form-urlencoded")).toArray());
	}

	/**
	 * http POST文本流
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return String
	 */
	public static String doPost(String url, String params, Header[] headers) {
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			post.setHeaders(headers);
			if (StringUtils.isNotBlank(params)) {
				StringEntity entity = new StringEntity(params, CHARSET_UTF8);
				entity.setContentEncoding(CHARSET_UTF8);
				entity.setContentType("application/json");// 发送json数据需要设置contentType
				post.setEntity(entity);
			}
			response = getHttpClient().execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity());// 返回json格式
			} else {
				logger.warn("HTTP POST文本流失败:" + url + "," + JSON.toJSONString(params) + ","
						+ response.getStatusLine().getStatusCode() + "," + response.getStatusLine().getReasonPhrase());

			}
		} catch (Exception e) {
			logger.error("HTTP POST文本流异常:" + url + "," + JSON.toJSONString(params), e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("HTTP流POST异常", e);
			}
		}
		return null;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param map
	 *            参数容器
	 * @return String
	 */
	public static String doPost(String url, Map<String, String> map) {
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			if (map != null) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();// 参数用
				for (Entry<String, String> entry : map.entrySet()) {
					params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				post.setEntity(new UrlEncodedFormEntity(params, CHARSET_UTF8));// 设置参数，及编码
			}
			response = getHttpClient().execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), CHARSET_UTF8);// 返回json格式
			} else {
				logger.warn("HTTP POST请求失败:" + url + "," + JSON.toJSONString(map) + ","
						+ response.getStatusLine().getStatusCode() + "," + response.getStatusLine().getReasonPhrase());
			}
		} catch (Exception e) {
			logger.error("HTTP POST异常:" + url + "," + JSON.toJSONString(map), e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error("HTTP POST异常", e);
			}
		}
		return null;
	}

}
