package org.study.nio.apache;

import java.io.File;
import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.protocol.BasicAsyncRequestConsumer;
import org.apache.http.nio.protocol.BasicAsyncResponseProducer;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.nio.protocol.HttpAsyncRequestConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestHandler;
import org.apache.http.nio.protocol.HttpAsyncRequester;
import org.apache.http.protocol.HttpContext;

/**
 * @Title: HttpNioClient.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 上午9:30:10
 * @see {@linkplain http://blog.csdn.net/a5987995329/article/details/76467673}
 */
public class HttpNioClient {

	public static void main(String[] args) {
		HttpAsyncRequestHandler<HttpRequest> rh = new HttpAsyncRequestHandler<HttpRequest>() {

			@Override
			public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context)
					throws HttpException, IOException {
				return new BasicAsyncRequestConsumer();
			}

			@Override
			public void handle(HttpRequest data, HttpAsyncExchange httpExchange, HttpContext context)
					throws HttpException, IOException {
				// TODO Auto-generated method stub
				HttpResponse response = httpExchange.getResponse();
				response.setStatusCode(HttpStatus.SC_OK);
				NFileEntity body = new NFileEntity(new File(""), ContentType.create("text/html", Consts.UTF_8));
				response.setEntity(body);
				httpExchange.submitResponse(new BasicAsyncResponseProducer(response));
			}
		};

	}

}
