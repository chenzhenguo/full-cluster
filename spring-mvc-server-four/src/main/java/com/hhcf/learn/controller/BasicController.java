package com.hhcf.learn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @Title: BasicController
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 下午2:25:41
 */
@Controller
@RequestMapping("/basic")
public class BasicController {

	/**
	 * 异常跳转页
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/exception")
	public String exceptionPage(Model model) {
		return "exception";
	}

}
