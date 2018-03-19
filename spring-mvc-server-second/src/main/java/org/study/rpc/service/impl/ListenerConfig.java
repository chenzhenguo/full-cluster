package org.study.rpc.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @see {@linkplain http://blog.csdn.net/wo541075754/article/details/71720984}
 * @Title: ListenerConfig
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月19日 下午6:47:16
 */
@Configuration
public class ListenerConfig {
	@Bean
	public ApplicationStartListener applicationStartListener() {
		return new ApplicationStartListener();
	}
}
