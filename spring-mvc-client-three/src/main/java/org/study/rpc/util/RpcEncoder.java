package org.study.rpc.util;

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
public class RpcEncoder<I> extends MessageToByteEncoder<Object> {
	private Class<?> genericClass;

	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}

	@Override
	protected void encode(ChannelHandlerContext arg0, Object in, ByteBuf out) throws Exception {
		System.out.println("rpc客户端:"+in.getClass().getName());
		// TODO Auto-generated method stub
		 if (genericClass.isInstance(in)) {
	            byte[] data = SerializationUtil.serialize(in);
	            out.writeInt(data.length);
	            out.writeBytes(data);
	        }
	}

}
