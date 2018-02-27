package org.study.nio.apache;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @Title: HttpClientUtils.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午1:53:42
 * @see {@linkplain https://www.cnblogs.com/zemliu/p/3719292.html}
 */
public class HttpClientUtils {

	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 3; i++) {
			final CloseableHttpAsyncClient httpAsyncClient = getHttpClient();

			doGet(httpAsyncClient, new FutureCallback<HttpResponse>() {

				/**
				 * 请求完成后调用
				 */
				public void completed(final HttpResponse response) {
					try {
						System.out.println(Thread.currentThread().getId() + ":completed->" + response.getStatusLine());
						org.apache.http.client.utils.HttpClientUtils.closeQuietly(response);
					} finally {
						try {
							httpAsyncClient.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				/**
				 * 请求失败后调用
				 */
				public void failed(final Exception ex) {
					try {
						System.out.println("->" + ex);
					} finally {
						try {
							httpAsyncClient.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				/**
				 * 请求取消后调用
				 */
				public void cancelled() {
					try {
						System.out.println(" cancelled");
					} finally {
						try {
							httpAsyncClient.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
//		}

//		Thread.sleep(5 * 1000);
		System.out.println("bbbbbbbbbbbbb");
	}

	private static final Logger logger = Logger.getLogger(HttpClientUtils.class);
	private static final String CHARSET_UTF8 = StandardCharsets.UTF_8.name();// 字符编码
	private static PoolingNHttpClientConnectionManager poolManager;// 连接池管理
	private static RequestConfig requestConfig;// 连接配置
	private static SSLContext sslcontext;

	/**
	 * 初始化连接池，spring容器初始化进行
	 */
	// @PostConstruct
	public void init() {
		try {
			sslcontext = SSLContexts.custom().build();
			ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
			poolManager = new PoolingNHttpClientConnectionManager(ioReactor);
			poolManager.setMaxTotal(200);// 连接池最大并发连接数 TODO 配置成系统常量
			poolManager.setDefaultMaxPerRoute(poolManager.getMaxTotal());// 单台主机的最大并发连接数
			requestConfig = RequestConfig.custom().setConnectTimeout(1000 * 20)// 表示建立连接的超时时间
					.setSocketTimeout(1000 * 20)// 数据传输处理时间
					.build();
		} catch (Exception e) {
			logger.error("httpAsyncClient factory初始化异常", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 提供HttpClient实例
	 */
	public static CloseableHttpAsyncClient getHttpClient() {
		CloseableHttpAsyncClient client = HttpAsyncClients.custom().setConnectionManager(poolManager)
				.setDefaultRequestConfig(requestConfig).setSSLContext(sslcontext).build();
		client.start();
		return client;
	}

	public static void doPost(String url, FutureCallback<HttpResponse> callback, Map<String, String> params)
			throws Exception {
		// 一个可复用的ioreactor, 负责生成SessionRequest并唤醒selector去做连接到目标网站的操作
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setMaxTotal(100);

		SSLContext sslcontext = SSLContexts.custom().build();
		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm)
				.setSSLContext(sslcontext).build();
		httpAsyncClient.start();
		HttpPost httppost = new HttpPost(url);
		if (params != null) {
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			Set<String> keys = params.keySet();
			for (String key : keys) {
				formParams.add(new BasicNameValuePair(key, params.get(key)));
			}
			httppost.setEntity(new UrlEncodedFormEntity(formParams, "utf-8"));
		}

		httpAsyncClient.execute(httppost, callback);
		System.out.println("结束");
		httpAsyncClient.close();
	}

	public static void doGet(CloseableHttpAsyncClient httpAsyncClient, FutureCallback<HttpResponse> callback)
			throws Exception {
//		String[] urisToGet = { "http://www.chinaso.com/", "http://www.so.com/", "http://www.qq.com/", };

//		for (final String uri : urisToGet) {
			final HttpGet httpget = new HttpGet("http://www.chinaso.com/");
			httpAsyncClient.execute(httpget, callback);
			System.out.println("结束准备……" + "http://www.chinaso.com/");
//		}
		System.out.println("结束准备……");
		System.out.println("结束");
//		Thread.sleep(5 * 1000);
		// httpAsyncClient.close();
		System.out.println("aaaaaaaaaaaaaaaaa");
	}

}
