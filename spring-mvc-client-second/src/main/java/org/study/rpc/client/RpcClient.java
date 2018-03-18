package org.study.rpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.study.rpc.model.RpcRequest;
import org.study.rpc.model.RpcResponse;
import org.study.rpc.util.RpcDecoder;
import org.study.rpc.util.RpcEncoder;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Title: RpcClient.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午2:06:32
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

	private String host;
	private int port;

	private RpcResponse response;

	private final Object obj = new Object();

	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, RpcResponse arg1) throws Exception {
		System.out.println("rpc客户端channelRead0:" + JSON.toJSONString(arg1));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("client caught exception", cause);
		ctx.close();
	}

	public RpcResponse send(RpcRequest request) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast(new RpcEncoder(RpcRequest.class)) // 将RPC请求进行编码（为了发送请求）
							.addLast(new RpcDecoder(RpcResponse.class)) // 将RPC响应进行解码（为了处理响应）
							.addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
				}
			}).option(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().writeAndFlush(request).sync();

			synchronized (obj) {
				obj.wait(); // 未收到响应，使线程等待
			}

			if (response != null) {
				future.channel().closeFuture().sync();
			}
			return response;
		} finally {
			group.shutdownGracefully();
		}
	}

}
