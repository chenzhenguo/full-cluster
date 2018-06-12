package com.hhcf.learn.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Title: LoginController
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:42:26
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-hello-world-example.html}
 */
@Controller
@RequestMapping
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * http://localhost:8180/spring-mvc-server-four/dbaaa.do
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/dba**")
	public String dbaPage(Model model) {
		model.addAttribute("title", "Spring Security Hello World");
		model.addAttribute("message", "This is protected page - Database Page!");
		return "admin";
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/hello.do
	 * 
	 * @return String
	 */
	@RequestMapping("/hello")
	public ModelAndView hello(ModelAndView model) {
		logger.info("hello:aaaaaaaaaaaaaaaaa");
		model.addObject("title", "Spring Security Custom Login Form");
		model.addObject("message", "This is welcome page!");
		model.setViewName("hello");
		return model;
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/admin.do
	 * 
	 * @return String
	 */
	@RequestMapping("/admin")
	public String admin(Model model) {
		logger.info("admin:aaaaaaaaaaaaaaaaa");
		model.addAttribute("title", "Spring Security Custom Login Form");
		model.addAttribute("message", "This is protected page!");
		return "admin";
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/login.do
	 * 
	 * @return String
	 */
	@RequestMapping("/login")
	public String login(Model model, @RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		logger.info("login:aaaaaaaaaaaaaaaaa");
		if (error != null) {
			model.addAttribute("error", "Invalid username and password!");
			return "error";
		}
		if (logout != null) {
			model.addAttribute("msg", "You've been logged out successfully.");
			return "logout";
		}
		return "login";
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/home.do
	 * 
	 * @return String
	 */
	@RequestMapping("/home")
	public String home(Model model) {
		logger.info("home:aaaaaaaaaaaaaaaaa");
		model.addAttribute("title", "Spring Security Hello World");
		model.addAttribute("message", "This is protected page!");
		return "home";
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/welcome.do
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping("/welcome")
	public String welcome(Model model) {
		logger.info("welcome:aaaaaaaaaaaaaaaaa");
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = principal instanceof String ? (String) principal
				: (principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : "");
		model.addAttribute("username", username);
		model.addAttribute("title", "Spring Security Hello World");
		model.addAttribute("message", "This is protected page!");
		return "welcome";
	}

	/**
	 * 登陆成功进行处理的方法
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginSucc")
	@ResponseBody
	public Map<String, Object> loginSucc(HttpServletRequest request) {
		logger.info("登录成功!");
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

	/**
	 * 登陆失败进行的操作
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/loginFail")
	@ResponseBody
	public Map<String, Object> loginFail(HttpServletRequest request) {
		logger.info("登录失败!");
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

}
