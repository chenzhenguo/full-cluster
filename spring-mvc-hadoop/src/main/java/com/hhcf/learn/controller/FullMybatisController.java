package com.hhcf.learn.controller;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hhcf.learn.service.FullService;

/**
 * 
 * @Title: MybatisController
 * @Description:mybatis 学习
 * @Author: zhaotf
 * @Since:2017年7月11日 上午9:21:46
 * @Version:1.0
 */
@Controller
@RequestMapping("/fullMybatis")
public class FullMybatisController {
	private static Logger logger = LogManager
			.getLogger(FullMybatisController.class);
	@Autowired
	private FullService fullService;

	/**
	 * 数据列表查询
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-hadoop/fullMybatis/findUsers.do
	 */
	@ResponseBody
	@RequestMapping(value = "/findUsers", method = RequestMethod.POST)
	public Object findUsers(HttpServletRequest request) {
		Iterator<?> param = request.getParameterMap().keySet().iterator();
		for (; param.hasNext();) {
			String key = param.next().toString();
			logger.info("request参数:" + key + "|" + request.getParameter(key));
		}

		logger.info("列表数据查询");
		return this.fullService.findUsers(request);
	}

	/**
	 * http://127.0.0.1:8080/spring-mvc-hadoop/fullMybatis/testConnect.do
	 */
	@ResponseBody
	@RequestMapping("/testConnect")
	public Object testConnect(HttpServletRequest request) {
		return fullService.testConnect(request);
	}
}
