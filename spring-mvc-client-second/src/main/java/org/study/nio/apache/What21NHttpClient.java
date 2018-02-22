package org.study.nio.apache;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
import org.apache.http.impl.nio.pool.BasicNIOConnPool;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestExecutor;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;

/**
 * @Title: What21NHttpClient.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午2:21:41
 * @see {@linkplain http://www.what21.com/sys/view/java_http-java_1476363115734.html}
 */
public class What21NHttpClient {

	public static void main(String[] args) throws IOException, Exception {
		// 创建HTTP协议处理
		HttpProcessor httpproc = HttpProcessorBuilder.create()
				// RequestContent，发起请求最重要的拦截器
				.add(new RequestContent())
				// RequestTargetHost，添加http host头
				.add(new RequestTargetHost()).add(new RequestConnControl())
				// 添加UserAgent
				.add(new RequestUserAgent("What21/1.1")).add(new RequestExpectContinue(true)).build();
		// 创建客户端HTTP协议处理器
		HttpAsyncRequestExecutor protocolHandler = new HttpAsyncRequestExecutor();
		// 创建客户端I/0事件分发
		final IOEventDispatch ioEventDispatch = new DefaultHttpClientIODispatch(protocolHandler,
				ConnectionConfig.DEFAULT);
		// 创建客户端I/O反应器
		final ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
		// Create HTTP connection pool
		// 创建HTTP连接池
		BasicNIOConnPool pool = new BasicNIOConnPool(ioReactor, ConnectionConfig.DEFAULT);
		// 数限2个连接总数
		pool.setDefaultMaxPerRoute(2);
		pool.setMaxTotal(2);

		// 运行I/O反应器单独线程
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					// Ready to go!
					ioReactor.execute(ioEventDispatch);
				} catch (InterruptedIOException ex) {
					System.err.println("线程被打断");
				} catch (IOException e) {
					System.err.println("I/O 错误: " + e.getMessage());
				}
				System.out.println("停止处理");
			}

		});
		// 启动线程
		t.start();

		// 创建异步HTTP请求
		HttpAsyncRequester requester = new HttpAsyncRequester(httpproc);

		// HTTP GET执行目标
		final HttpHost target = new HttpHost("www.baidu.com", 80, "http");

		final CountDownLatch latch = new CountDownLatch(1);
		// HttpRequest的实现，这里表示使用GET方式执行URI
		BasicHttpRequest request = new BasicHttpRequest("GET", "/");
		// 提供设置分配属性和获取属性，HttpContext接口的实现
		HttpCoreContext coreContext = HttpCoreContext.create();
		requester.execute(new BasicAsyncRequestProducer(target, request), new BasicAsyncResponseConsumer(), pool,
				coreContext, new FutureCallback<HttpResponse>() {
					/**
					 * 成功
					 */
					public void completed(final HttpResponse response) {
						latch.countDown();
						try {
							System.out.println("成功:" + target + "->" + response.getStatusLine());
							// System.out.println("result ->" +
							// EntityUtils.toString(response.getEntity()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					/**
					 * 失败
					 */
					public void failed(final Exception ex) {
						latch.countDown();
						System.out.println("失败:" + target + "->" + ex);
					}

					/**
					 * 取消
					 */
					public void cancelled() {
						latch.countDown();
						System.out.println("失败:" + target + " cancelled");
					}

				});

		latch.await();
		System.out.println("Shutting down I/O reactor");
		ioReactor.shutdown();
		System.out.println("完成");
	}

}
