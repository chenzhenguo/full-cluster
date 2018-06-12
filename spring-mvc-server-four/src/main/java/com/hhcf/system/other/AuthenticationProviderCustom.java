package com.hhcf.system.other;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.hhcf.learn.service.UserService;

/**
 * @Title: AuthenticationProviderCustom
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 下午3:00:20
 * @see {@linkplain https://www.jianshu.com/p/a7ea68c35995}
 */
public class AuthenticationProviderCustom implements AuthenticationProvider {
	private Logger logger = LoggerFactory.getLogger(AuthenticationProviderCustom.class);
	@Resource
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return false;
	}

}
