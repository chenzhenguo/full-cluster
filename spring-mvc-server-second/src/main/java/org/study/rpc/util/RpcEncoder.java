package org.study.rpc.util;

import org.study.rpc.medol.RpcResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Title: RpcEncoder.java
 * @Description: TODO
 * @author zhaotf
 * @param <I>
 * @date 2018年3月18日 下午1:11:08
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public class RpcEncoder extends MessageToByteEncoder<RpcResponse> {
	private Class<?> genericClass;

	public RpcEncoder(Class<?> genericClass) {
		System.out.println("RpcEncoder-aaaaaaaaaaaaaaa"); 
		this.genericClass = genericClass;
	}

	// @Override
	// protected void encode(ChannelHandlerContext arg0, Object in, ByteBuf out)
	// throws Exception {
	// // TODO Auto-generated method stub
	// if (genericClass.isInstance(in)) {
	// byte[] data = SerializationUtil.serialize(in);
	// out.writeInt(data.length);
	// out.writeBytes(data);
	// }
	// }

	@Override
	protected void encode(ChannelHandlerContext arg0, RpcResponse in, ByteBuf out) throws Exception {
		if (genericClass.isInstance(in)) {
			byte[] data = SerializationUtil.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
		System.out.println("rpc服务端-RpcEncoder:" + in);
	}

}
