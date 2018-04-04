package com.hhcf.backend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hhcf.backend.service.FullService;

/**
 * 
 * @Title: HttpAsyncController
 * @Description:HttpAsync测试
 * @Author: zhaotf
 * @Since:2018年3月12日 下午2:06:48
 */
@Controller
@RequestMapping("/httpAsync")
public class HttpAsyncController {
	@Autowired
	private FullService fullService;

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
	 * @see http://127.0.0.1:8080/spring-mvc-server-first/fullBase/getModelData.
	 *      do
	 */
	@ResponseBody
	@RequestMapping("/getModelData")
	public Object getModelData(HttpServletRequest request, String city, String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityname", "springMVC跨域处理");
		map.put("code", code + 66);
		System.out.println("后台处理:" + JSON.toJSONString(map));
		return map;
	}
}
