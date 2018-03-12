package org.study.nio.apache;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @Title: HttpAsyncClientUtil
 * @Description:http非阻塞式请求客户端，连接已池化处理
 * @Author: zhaotf
 * @Since:2018年2月27日 下午7:07:00
 */
public class HttpAsyncClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static final String CHARSET_UTF8 = StandardCharsets.UTF_8.name();// 字符编码
	private static PoolingNHttpClientConnectionManager poolManager;// 连接池管理
	private static RequestConfig requestConfig;// 连接配置

	/**
	 * 初始化连接池，spring容器初始化进行
	 */
	@PostConstruct
	public void init() {
		try {
			ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
			poolManager = new PoolingNHttpClientConnectionManager(ioReactor);
			poolManager.setMaxTotal(100);// 连接池最大并发连接数
			poolManager.setDefaultMaxPerRoute(100);// 单台主机的最大并发连接数
			requestConfig = RequestConfig.custom().setConnectTimeout(5 * 1000)// 表示建立连接的超时时间
					.setSocketTimeout(5 * 1000)// 数据传输处理时间
					.setConnectionRequestTimeout(5 * 1000)// 从连接池中获取连接的超时时间,毫秒
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
				.setDefaultRequestConfig(requestConfig).build();
		client.start();
		return client;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param map
	 *            参数
	 * @param callback
	 *            回调函数
	 */
	public static void doPost(final String url, final Map<String, String> map,
			final MyFutureCallback<HttpResponse> callback) {
		try {
			HttpPost post = new HttpPost(url);
			if (map != null) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();// 参数用
				for (Entry<String, String> entry : map.entrySet()) {
					params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				post.setEntity(new UrlEncodedFormEntity(params, CHARSET_UTF8));// 设置参数，及编码
			}
			final CloseableHttpAsyncClient client = getHttpClient();
			client.execute(post, new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse result) {
					try {
						callback.completed(result);
					} catch (Exception e) {
						logger.error("HttpAsyncClientUtil POST异常:" + url + "," + JSON.toJSONString(map), e);
						throw new RuntimeException(e);
					} finally {
						try {
							org.apache.http.client.utils.HttpClientUtils.closeQuietly(result);// 关闭资源
							client.close();// 关闭连接
						} catch (IOException e) {
							logger.error("HttpAsyncClientUtil POST关闭资源异常", e);
						}
					}
				}

				@Override
				public void failed(Exception ex) {
					try {
						callback.failed(ex);
						logger.error(ex.getMessage(), ex);
					} catch (Exception e) {
						logger.error("HttpAsyncClientUtil POST异常:" + url + "," + JSON.toJSONString(map), e);
					} finally {
						try {
							client.close();// 关闭连接
						} catch (IOException e) {
							logger.error("HttpAsyncClientUtil POST关闭资源异常", e);
						}
					}
				}

				@Override
				public void cancelled() {
					try {
						callback.cancelled();
					} catch (Exception e) {
						logger.error("HttpAsyncClientUtil POST异常:" + url + "," + JSON.toJSONString(map), e);
					} finally {
						try {
							client.close();// 关闭连接
						} catch (IOException e) {
							logger.error("HttpAsyncClientUtil POST关闭资源异常", e);
						}
					}
				}
			});
		} catch (Exception e) {
			logger.error("HttpAsyncClientUtil POST异常:" + url + "," + JSON.toJSONString(map), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * GET请求
	 * 
	 * @param url
	 * @param callback
	 *            回调函数
	 */
	public static void doGet(final String url, final MyFutureCallback<HttpResponse> callback) {
		try {
			final CloseableHttpAsyncClient client = getHttpClient();
			client.execute(new HttpGet(url), new FutureCallback<HttpResponse>() {

				@Override
				public void completed(HttpResponse result) {
					try {
						callback.completed(result);
					} catch (Exception e) {
						logger.error("HttpAsyncClientUtil GET异常:" + url, e);
					} finally {
						try {
							org.apache.http.client.utils.HttpClientUtils.closeQuietly(result);// 关闭资源
							client.close();// 关闭连接
						} catch (IOException e) {
							logger.error("HttpAsyncClientUtil GET关闭资源异常", e);
						}
					}
				}

				@Override
				public void failed(Exception ex) {
					try {
						callback.failed(ex);
						logger.error(ex.getMessage(), ex);
					} catch (Exception e) {
						logger.error("HttpAsyncClientUtil GET异常:" + url, e);
					} finally {
						try {
							client.close();// 关闭连接
						} catch (IOException e) {
							logger.error("HttpAsyncClientUtil GET关闭资源异常", e);
						}
					}
				}

				@Override
				public void cancelled() {
					try {
						callback.cancelled();
					} catch (Exception e) {
						logger.error("HttpAsyncClientUtil GET异常:" + url, e);
					} finally {
						try {
							client.close();// 关闭连接
						} catch (IOException e) {
							logger.error("HttpAsyncClientUtil GET关闭资源异常", e);
						}
					}
				}
			});
		} catch (Exception e) {
			logger.error("HttpAsyncClientUtil GET异常:" + url, e);
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ver", "1.00");
		map.put("code", "freeze");
		map.put("mchnt_cd", "0002900F0096235");
		map.put("mchnt_txn_ssn", "Ssn0123456789");
		map.put("amt", "10000");
		map.put("project_no", "NZWZ00000000000001");
		map.put("busi_tp", "00");
		map.put("signature",
				"auK8wfMTwG8ObQ2HC0J3KnSzgSGAplpYhIzU0pksT1Zzhb22hmVllM+dgjfXy5OriA6+0xzlr0ByfFcv6EOmcxduZ0Aa84Ouui9G1zYbkiGEv/AG+0VwqYYqZUVCEbEEnIvyyqtyKB4RyBZxg8HPGaBPRls6pTH8Lc5i5m1aaEA=");

		doPost("http://116.239.4.195:8090/control.action", map, new MyFutureCallback<HttpResponse>() {

			@Override
			public void completed(HttpResponse response) {
				String rsltxml = null;
				try {
					// System.out.println(Thread.currentThread().getId() +
					// ":completed->" + response.getStatusLine());
					// rsltxml = EntityUtils.toString(response.getEntity(),
					// "utf-8");
					// logger.info("aaa:返回字符串:{}", rsltxml);
					// String jsontext = ZxbUtil.xml2json(rsltxml);
					// logger.info("bbb:返回字符串:{}", jsontext);
					// JSONObject obj = JSON.parseObject(jsontext);
					// System.out.println("ccc:返回字符串:" + obj.toJSONString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Exception ex) {
				logger.error("HttpAsyncClientUtil调用 失败", ex);
			}

			@Override
			public void cancelled() {
				logger.warn("HttpAsyncClientUtil调用取消，{}", "aaa");
			}
		});

		System.out.println("tttttttttttt");
	}

}
