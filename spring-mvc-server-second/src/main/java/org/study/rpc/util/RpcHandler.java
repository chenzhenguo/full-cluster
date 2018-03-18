package org.study.rpc.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.rpc.medol.RpcRequest;
import org.study.rpc.medol.RpcResponse;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * @Title: RpcHandler.java
 * @Description: TODO
 * @author zhaotf
 * @param <I>
 * @date 2018年3月18日 下午1:22:11
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcHandler.class);
	private final Map<String, Object> handlerMap;

	public RpcHandler(Map<String, Object> handlerMap) {
		System.out.println("RpcHandler-aaaaaaaaaaaaaa");
		this.handlerMap = handlerMap;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.error("rpc服务端RpcHandler.exceptionCaught:", cause);
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
		System.out.println("rpc服务端RpcHandler.channelRead0:" + request);
		RpcResponse response = new RpcResponse();
		response.setRequestId(request.getRequestId());
		try {
			Object result = handle(request);
			response.setResult(result);
		} catch (Throwable t) {
			response.setError(t);
		}
		ctx.writeAndFlush(request).addListener(ChannelFutureListener.CLOSE);
	}

	private Object handle(RpcRequest request) throws Throwable {
		String className = request.getClassName();
		Object serviceBean = handlerMap.get(className);

		Class<?> serviceClass = serviceBean.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();

		/*
		 * Method method = serviceClass.getMethod(methodName, parameterTypes);
		 * method.setAccessible(true); return method.invoke(serviceBean,
		 * parameters);
		 */

		FastClass serviceFastClass = FastClass.create(serviceClass);
		FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
		return serviceFastMethod.invoke(serviceBean, parameters);
	}

}
