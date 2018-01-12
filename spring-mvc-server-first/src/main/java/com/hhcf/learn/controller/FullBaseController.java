package com.hhcf.learn.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @Title: FullBaseController.java
 * @Description: TODO springMVC跨域处理
 * @author zhaotf
 * @date 2017年12月3日 下午3:47:00
 */
@Controller
@RequestMapping("/fullBase")
public class FullBaseController {
	static Logger logger = LoggerFactory.getLogger(FullBaseController.class);

	/**
	 * @see http://127.0.0.1:8080/spring-mvc-server-first/fullBase/initModel.do
	 */
	@RequestMapping(value = "/initModel")
	public String initModel(HttpServletRequest request) {
		return "initModel";
	}

	/**
	 * 
	 * 
	 * @see http://127.0.0.1:8080/spring-mvc-server-first/fullBase/getModelData.do
	 */
	@ResponseBody
	@RequestMapping("/getModelData")
	public Object getModelData(HttpServletRequest request, String city, String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityname", "springMVC跨域处理");
		map.put("code", code + 66);
		logger.info("后台处理:" + JSON.toJSONString(map));
		return map;
	}

}
