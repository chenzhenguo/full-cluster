package org.study.rpc.service.impl;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @see {@linkplain http://blog.csdn.net/wo541075754/article/details/71720984}
 * @Title: ApplicationStartListener
 * @Description: 
 * @Author: zhaotf  
 * @Since:2018年3月19日  下午6:03:42
 */
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub  
	       System.out.println("我的父容器为：" + event.getApplicationContext().getParent());
	        System.out.println("初始化时我被调用了。");
	}

}
