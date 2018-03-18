package org.study.rpc.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Title: ClientHandler.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午1:40:34
 * @see {@linkplain http://blog.csdn.net/coder_py/article/details/72825077}
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 客户端读取服务端信息
	 */
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
		System.out.println("客户端开始读取服务端过来的信息");
		ByteBuf buf = (ByteBuf) arg1;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		System.out.println(new String(bytes));

	}

	/**
	 * 客户端连接到服务端后进行
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端传给服务端");
		String string = "to Server";
		ByteBuf buf = Unpooled.buffer(string.length());
		buf.writeBytes(string.getBytes());
		ctx.writeAndFlush(buf);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

}
