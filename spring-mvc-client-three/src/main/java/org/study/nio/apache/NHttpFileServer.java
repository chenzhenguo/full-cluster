package org.study.nio.apache;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.bootstrap.HttpServer;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.ssl.SSLContexts;

/**
 * @Title: NHttpFileServer.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午1:30:39
 * @see {@linkplain http://tieba.baidu.com/p/4714055819}
 */
public class NHttpFileServer {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Please specify document root directory");
			System.exit(1);
		}
		// Document root directory 文档根目录
		File docRoot = new File(args[0]);
		int port = 8080;
		if (args.length >= 2) {
			port = Integer.parseInt(args[1]);
		}

		SSLContext sslcontext = null;
		if (port == 8443) {
			// Initialize SSL context SSL上下文初始化
			URL url = NHttpFileServer.class.getResource("/my.keystore");
			if (url == null) {
				System.out.println("Keystore not found");
				System.exit(1);
			}
			sslcontext = SSLContexts.custom().loadKeyMaterial(url, "secret".toCharArray(), "secret".toCharArray())
					.build();
		}

		IOReactorConfig config = IOReactorConfig.custom().setSoTimeout(15000).setTcpNoDelay(true).build();

		final HttpServer server = ServerBootstrap.bootstrap().setListenerPort(port).setServerInfo("Test/1.1")
				.setIOReactorConfig(config).setSslContext(sslcontext).setExceptionLogger(ExceptionLogger.STD_ERR)
				.registerHandler("*", new HttpFileHandler(docRoot)).create();

		server.start();
		server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.shutdown(5, TimeUnit.SECONDS);
			}
		});

	}

	static class HttpFileHandler implements HttpAsyncRequestHandler<HttpRequest> {

		private final File docRoot;

		public HttpFileHandler(final File docRoot) {
			super();
			this.docRoot = docRoot;
		}

		public HttpAsyncRequestConsumer<HttpRequest> processRequest(final HttpRequest request,
				final HttpContext context) {
			// Buffer request content in memory for simplicity 简单存储器中的缓冲请求内容
			return new BasicAsyncRequestConsumer();
		}

		public void handle(final HttpRequest request, final HttpAsyncExchange httpexchange, final HttpContext context)
				throws HttpException, IOException {
			HttpResponse response = httpexchange.getResponse();
			handleInternal(request, response, context);
			httpexchange.submitResponse(new BasicAsyncResponseProducer(response));
		}

		private void handleInternal(final HttpRequest request, final HttpResponse response, final HttpContext context)
				throws HttpException, IOException {

			String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
			if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
				throw new MethodNotSupportedException(method + " method not supported");
			}

			String target = request.getRequestLine().getUri();
			final File file = new File(this.docRoot, URLDecoder.decode(target, "UTF-8"));
			if (!file.exists()) {

				response.setStatusCode(HttpStatus.SC_NOT_FOUND);
				NStringEntity entity = new NStringEntity(
						"<html><body><h1>File" + file.getPath() + " not found</h1></body></html>",
						ContentType.create("text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("File " + file.getPath() + " not found");

			} else if (!file.canRead() || file.isDirectory()) {

				response.setStatusCode(HttpStatus.SC_FORBIDDEN);
				NStringEntity entity = new NStringEntity("<html><body><h1>Access denied</h1></body></html>",
						ContentType.create("text/html", "UTF-8"));
				response.setEntity(entity);
				System.out.println("Cannot read file " + file.getPath());

			} else {

				HttpCoreContext coreContext = HttpCoreContext.adapt(context);
				HttpConnection conn = coreContext.getConnection(HttpConnection.class);
				response.setStatusCode(HttpStatus.SC_OK);
				NFileEntity body = new NFileEntity(file, ContentType.create("text/html"));
				response.setEntity(body);
				System.out.println(conn + ": serving file " + file.getPath());
			}
		}

	}

}
