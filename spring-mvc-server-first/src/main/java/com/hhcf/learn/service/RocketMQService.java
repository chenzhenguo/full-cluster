package com.hhcf.learn.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Title: RocketMQService
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月12日 下午4:56:06
 */
public interface RocketMQService {

	public Object sendMessage(HttpServletRequest request) throws Exception;

}
