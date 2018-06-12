package com.hhcf.system.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hhcf.learn.dao.UserMapper;
import com.hhcf.learn.model.UserModel;

/**
 * @Title: JadeUserPwdAuthFilter
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:30:05
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 */
public class JadeUserPwdAuthFilter extends UsernamePasswordAuthenticationFilter {

	public static final String USERNAME = "userName";
	public static final String PASSWORD = "userPassword";

	@Autowired
	private UserMapper userDao;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String userName = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);

		UserModel user = userDao.findUserByUserName(userName);
		System.out.println("username: " + user.getUsername());
		System.out.println("password: " + user.getPassword());

		// 验证用户是否被启用
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);
		// 允许子类设置详细属性
		setDetails(request, authRequest);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
