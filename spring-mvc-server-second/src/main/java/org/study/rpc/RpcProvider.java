package org.study.rpc;

import org.study.rpc.sample.service.HelloService;
import org.study.rpc.sample.service.impl.HelloServiceImpl;

/**
 * @Title: RpcProvider.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午6:08:08
 * @see {@linkplain http://blog.csdn.net/gg_gogoing/article/details/47442635}
 */
public class RpcProvider {

	public static void main(String[] args) throws Exception {
		HelloService service = new HelloServiceImpl();
		RpcFramework.export(service, 1234);
	}

}
