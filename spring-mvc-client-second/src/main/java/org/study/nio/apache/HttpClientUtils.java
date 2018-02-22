package org.study.nio.apache;

import java.util.concurrent.CountDownLatch;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.ssl.SSLContexts;

/**
 * @Title: HttpClientUtils.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午1:53:42
 * @see {@linkplain https://www.cnblogs.com/zemliu/p/3719292.html}
 */
public class HttpClientUtils {

	public static void main(String[] args) throws Exception {
		// 一个可复用的ioreactor, 负责生成SessionRequest并唤醒selector去做连接到目标网站的操作
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor);
		cm.setMaxTotal(100);

		SSLContext sslcontext = SSLContexts.custom().build();
		CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm)
				.setSSLContext(sslcontext).build();
		httpAsyncClient.start();
		String[] urisToGet = { "http://www.chinaso.com/", "http://www.so.com/", "http://www.qq.com/", };

		final CountDownLatch latch = new CountDownLatch(urisToGet.length);
		for (final String uri : urisToGet) {
			final HttpGet httpget = new HttpGet(uri);
			httpAsyncClient.execute(httpget, new FutureCallback<HttpResponse>() {

				public void completed(final HttpResponse response) {
					latch.countDown();
					System.out.println(httpget.getRequestLine() + "->" + response.getStatusLine());
				}

				public void failed(final Exception ex) {
					latch.countDown();
					System.out.println(httpget.getRequestLine() + "->" + ex);
				}

				public void cancelled() {
					latch.countDown();
					System.out.println(httpget.getRequestLine() + " cancelled");
				}

			});
		}
		System.out.println("结束准备……");
		latch.await();
		System.out.println("结束");
		httpAsyncClient.close();
	}

}
