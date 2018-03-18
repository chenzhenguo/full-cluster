package org.study.rpc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.study.annotation.RpcService;
import org.study.rpc.sample.service.HelloService;

/**
 * @Title: HelloServiceImpl.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午12:15:26
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
@RpcService(value = HelloService.class)
public class HelloServiceImpl implements HelloService {

	@Override
	public Map<String, Object> hello(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "rpc-server-zookeeper" + Thread.currentThread().getId());
		map.put("now-time", new Date());
		map.put("age", 33);
		return map;
	}

	@Override
	public Object sayHi(String string) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", "rpc-server-local" + Thread.currentThread().getId());
		map.put("now-time", new Date());
		map.put("age", 33);
		return map;
	}

}
