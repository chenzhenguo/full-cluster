package org.study.rpc.sample.service;

import java.util.Map;

/**
 * @Title: HelloService.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月18日 下午12:13:49
 * @see {@linkplain http://blog.csdn.net/jek123456/article/details/53200613}
 */
public interface HelloService {

	public Map<String, Object> hello(String name);

	public Object sayHi(String string);

}
