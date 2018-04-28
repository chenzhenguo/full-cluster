package com.hhcf.backend.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hhcf.backend.model.UserModel;

/**
 * @Title: IndexController.java
 * @Description: TODO(用一句话描述该文件做什么)
 * @author zhaotf
 * @date 2018年4月27日 下午3:21:29
 * @see {@linkplain https://www.cnblogs.com/andyfengzp/p/6434287.html}
 * @see {@linkplain https://blog.csdn.net/a60782885/article/details/70244305}
 */
@Controller
@RequestMapping("/index")
public class IndexController {

	private final Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();

	/**
	 * 主页面
	 * 
	 * @see http://localhost:8080/spring-mvc-client-second/index/main.do
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		String sessionId = (String) session.getAttribute("sessionId");
		System.out.println("main方法 sessionId:" + sessionId);
		if (StringUtils.isNotBlank(sessionId)) { // sessionId不为空
			model.addAttribute("usertext", sessionId);
			return "index";
		} else { // sessionId为空,重定向到 登录页面
			return "redirect:/index/login.do";
		}
	}

	/**
	 * 登录
	 * 
	 * @see http://localhost:8080/spring-mvc-client-second/index/doLogin.do
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/doLogin")
	public String doLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionId = UUID.randomUUID().toString();
		System.out.println("doLogin方法： " + sessionId);
		session.setAttribute("sessionId", sessionId);
		// 登录成功后，重定向到主页
		return "redirect:/index/main.do";
	}

	/**
	 * 登录页面
	 * 
	 * @param request
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, String username) {
		System.out.println("login方法:" + username);
		request.getSession().setAttribute("user", gson.toJson(new UserModel(username, "123456")));
		return "login";
	}

	/**
	 * 主页备用
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(HttpServletRequest request, Model model) {
		System.out.println("index方法");
		UserModel user = gson.fromJson(request.getSession().getAttribute("user").toString(), UserModel.class);
		model.addAttribute("user", user);
		return "index";
	}

}
