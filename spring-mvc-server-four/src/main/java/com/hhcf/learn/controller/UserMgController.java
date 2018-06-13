package com.hhcf.learn.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hhcf.learn.model.UserModel;
import com.hhcf.learn.model.UserProfileModel;
import com.hhcf.learn.service.UserService;

/**
 * @Title: UserMgController
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 下午2:40:26
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate.html}
 */
@Controller
@RequestMapping("/userMg")
public class UserMgController {
	private Logger logger = LoggerFactory.getLogger(UserMgController.class);
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/newUser", method = RequestMethod.GET)
	public String newRegistration(HttpServletRequest request,ModelMap model) {
		UserModel user = new UserModel();
		model.addAttribute("user", user);
		return "newuser";
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/userMg/saveUser.do
	 * 
	 * @param username
	 * @param password
	 * @return Object
	 */
	@ResponseBody
	@RequestMapping("/saveUser")
	public Object saveUser(HttpServletRequest request,String username, String password) {
		logger.info("用户信息录入:" + username + "," + password);
		return "成功";
	}

	/**
	 * http://localhost:8180/spring-mvc-server-four/userMg/saveRegistration.do
	 * 
	 * @param user
	 * @param result
	 * @param model
	 * @return String
	 */
	@ResponseBody
	@RequestMapping("/saveRegistration")
	public String saveRegistration(UserModel user) {
		logger.info("用户信息录入:" + JSON.toJSONString(user));
		userService.save(user);

		// System.out.println("First Name : " + user.getFirstName());
		// System.out.println("Last Name : " + user.getLastName());
		// System.out.println("SSO ID : " + user.getUsername());
		// System.out.println("Password : " + user.getPassword());
		// System.out.println("Email : " + user.getEmail());
		// System.out.println("Checking UsrProfiles....");
		// if (user.getUserProfiles() != null) {
		// for (UserProfileModel profile : user.getUserProfiles()) {
		// System.out.println("Profile : " + profile.getType());
		// }
		// }

		// model.addAttribute("success", "UserModel " + user.getFirstName() + "
		// has been registered successfully");
		return "registrationsuccess";
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

	// @ModelAttribute("roles")
	// public List<UserProfileModel> initializeProfiles() {
	// return userProfileService.findAll();
	// }

}
