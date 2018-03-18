package org.study.rpc.client;

import java.lang.reflect.Method;
import java.util.UUID;

import org.study.rpc.model.RpcRequest;
import org.study.rpc.model.RpcResponse;
import org.study.rpc.registry.ServiceDiscovery;

import com.alibaba.fastjson.JSON;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;

/**
 * @Title: RpcProxy.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午1:59:39
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcProxy {
	private String serverAddress;
	private ServiceDiscovery serviceDiscovery;

	public RpcProxy(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public RpcProxy(ServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<?> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
						request.setRequestId(UUID.randomUUID().toString());
						request.setClassName(method.getDeclaringClass().getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);

						if (serviceDiscovery != null) {
							serverAddress = serviceDiscovery.discover(); // 发现服务
						}

						System.out.println("rpc客户端InvocationHandler.invoke:" + proxy.getClass().getName() + "|"
								+ JSON.toJSONString(method) + "|" + JSON.toJSONString(args));
						String[] array = serverAddress.split(":");
						System.out.println(
								"rpc客户端InvocationHandler.invoke:" + serverAddress + "," + JSON.toJSONString(array));

						String host = array[0];
						System.out.println("rpc客户端aaaaaaaaaaaaaa");
						int port = Integer.parseInt(array[1]);
						System.out.println("rpc客户端bbbbbbbbbbbbbb");

						RpcClient client = new RpcClient(host, port); // 初始化RPC客户端
						System.out.println("rpc客户端cccccccccccccc");
						RpcResponse response = client.send(request); // 通过RPC客户端发送RPC请求并获取RPC响应
						System.out.println("rpc客户端dddddddddddddd");

						if (response.getError() != null) {
							response.getError().printStackTrace();
							throw response.getError();
						} else {
							return response.getResult();
						}
					}
				});
	}

}
