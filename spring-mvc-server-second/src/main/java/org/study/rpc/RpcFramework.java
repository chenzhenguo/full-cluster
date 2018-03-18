package org.study.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Title: RpcFramework.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午6:07:11
 * @see {@linkplain http://blog.csdn.net/gg_gogoing/article/details/47442635}
 */
public class RpcFramework {

	/**
	 * 暴露服务
	 * 
	 * @param service
	 *            服务实现
	 * @param port
	 *            服务端口
	 * @throws Exception
	 */
	public static void export(final Object service, int port) throws Exception {
		if (service == null)
			throw new IllegalArgumentException("service instance == null");
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("Invalid port " + port);
		System.out.println("Export service " + service.getClass().getName() + " on port " + port);
		ServerSocket server = new ServerSocket(port);// 前面都是验证调用是否符合规则，这里在被调用端开一个服务。下面就是死循环运行。
		for (;;) {
			try {
				final Socket socket = server.accept();
				new Thread(new Runnable() {// 对每一个请求new一个线程，匿名类
					@Override
					public void run() {
						try {
							try {
								ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
								try {
									String methodName = input.readUTF();
									Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
									Object[] arguments = (Object[]) input.readObject();
									ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());// 接收客户端传来的方法名、参数类型、参数
									try {
										Method method = service.getClass().getMethod(methodName, parameterTypes);// 在本地生成对应的方法，
										Object result = method.invoke(service, arguments);// 调用
										output.writeObject(result);// 返回结果
									} catch (Throwable t) {
										output.writeObject(t);
									} finally {
										output.close();
									}
								} finally {
									input.close();
								}
							} finally {
								socket.close();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 引用服务
	 * 
	 * @param <T>
	 *            接口泛型
	 * @param interfaceClass
	 *            接口类型
	 * @param host
	 *            服务器主机名
	 * @param port
	 *            服务器端口
	 * @return 远程服务
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T refer(final Class<T> interfaceClass, final String host, final int port) throws Exception {
		if (interfaceClass == null)
			throw new IllegalArgumentException("Interface class == null");
		if (!interfaceClass.isInterface())
			throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
		if (host == null || host.length() == 0)
			throw new IllegalArgumentException("Host == null!");
		if (port <= 0 || port > 65535)
			throw new IllegalArgumentException("Invalid port " + port);
		System.out.println("Get remote service " + interfaceClass.getName() + " from server " + host + ":" + port);
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {// 用动态代理的方法进行包装，看起来是在调用一个方法，其实在内部通过socket通信传到服务器，并接收运行结果
					public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
						Socket socket = new Socket(host, port);
						try {
							ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
							try {
								output.writeUTF(method.getName());
								output.writeObject(method.getParameterTypes());
								output.writeObject(arguments);
								ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
								try {
									Object result = input.readObject();
									if (result instanceof Throwable) {
										throw (Throwable) result;
									}
									return result;// 返回结果
								} finally {
									input.close();
								}
							} finally {
								output.close();
							}
						} finally {
							socket.close();
						}
					}
				});
	}

}
