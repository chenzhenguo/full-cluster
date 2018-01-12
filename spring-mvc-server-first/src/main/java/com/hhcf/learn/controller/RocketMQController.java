package com.hhcf.learn.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hhcf.learn.service.RocketMQService;

/**
 * 
 * @Title: RocketMQController
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月12日 下午4:47:05
 */
@Controller
@RequestMapping("/rocketMQ")
public class RocketMQController {
	@Autowired
	private RocketMQService rocketMQService;

	/**
	 * http://127.0.0.1:8180/spring-mvc-server-first/rocketMQ/sendMessage.do
	 */
	@RequestMapping("/sendMessage")
	public Object sendMessage(HttpServletRequest request) throws Exception {
		return rocketMQService.sendMessage(request);
	}

}
