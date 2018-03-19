package org.study.rpc.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.study.annotation.RpcService;
import org.study.rpc.medol.RpcRequest;
import org.study.rpc.medol.RpcResponse;
import org.study.rpc.registry.service.ServiceRegistry;
import org.study.rpc.util.RpcDecoder;
import org.study.rpc.util.RpcEncoder;
import org.study.rpc.util.RpcHandler;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Title: RpcServer.java
 * @Description: TODO 实现 RPC 服务器
 * @author zhaotf
 * @date 2018年3月18日 下午12:40:32
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcServer implements ApplicationContextAware, InitializingBean,BeanPostProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
	private String serverAddress;
	private ServiceRegistry serviceRegistry;
	private Map<String, Object> handlerMap = new HashMap<>(); // 存放接口名与服务对象之间的映射关系

	public RpcServer(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
		this.serverAddress = serverAddress;
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel channel) throws Exception {
							System.out.println("aaaaaaaaaaaaaaaaaaaa");
							channel.pipeline().addLast(new RpcDecoder(RpcRequest.class)) // 将RPC请求进行解码（为了处理请求）
									.addLast(new RpcEncoder(RpcResponse.class)) // 将RPC响应进行编码（为了返回响应）
									.addLast(new RpcHandler(handlerMap)) // 处理RPC请求
							;
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			String[] array = serverAddress.split(":");
			System.out.println("rpc服务端afterPropertiesSet:" + serverAddress + "," + JSON.toJSONString(array));
			String host = array[0];
			int port = Integer.parseInt(array[1]);

			ChannelFuture future = bootstrap.bind(host, port).sync();
			LOGGER.debug("server started on port {}", port);

			if (serviceRegistry != null) {
				serviceRegistry.register(serverAddress); // 注册服务地址
			}

			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		// TODO Auto-generated method stub
		Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class); // 获取所有带有RpcService注解的SpringBean
		LOGGER.info("setApplicationContext:"+JSON.toJSONString(serviceBeanMap));
		if (MapUtils.isNotEmpty(serviceBeanMap)) {
			for (Object serviceBean : serviceBeanMap.values()) {
				String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
				LOGGER.info("setApplicationContext:"+interfaceName+","+serviceBean.getClass().getName());
				handlerMap.put(interfaceName, serviceBean);
			}
		}
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		LOGGER.info("postProcessBeforeInitialization:"+bean+","+beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub  
		
//        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
//        if (methods != null) {
//            for (Method method : methods) {
//            	RpcService rpcService = AnnotationUtils.findAnnotation(method, RpcService.class);
//                // process
//            }
//        }
        
		LOGGER.info("postProcessAfterInitialization:"+bean+","+beanName);
		RpcService rpcService = AnnotationUtils.findAnnotation(bean.getClass(), RpcService.class);
		if(rpcService != null) {
			handlerMap.put(bean.getClass().getName(), bean);
		}
		
        return bean;
	}




}
