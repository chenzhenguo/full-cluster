package com.hhcf.learn.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.learn.controller.LoginController;
import com.hhcf.learn.dao.UserMapper;
import com.hhcf.learn.model.UserModel;
import com.hhcf.learn.service.UserService;

/**
 * @Title: UserServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:45:34
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 */
@Service("userDetailService")
@Transactional
public class UserServiceImpl implements UserService {
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("loadUserByUsername:" + username);
		UserModel userModel = null;
		try {
			userModel = userMapper.findUserByUserName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (userModel == null) {
			throw new UsernameNotFoundException("用户名或密码不正确!");
		}
		logger.info("username:" + userModel.getUsername() + ",password:" + userModel.getPassword());
		User user = new User(userModel.getUsername(),userModel.getPassword(),getGrantedAuthorities(userModel));
		return userModel;
	}

	
	private List<GrantedAuthority> getGrantedAuthorities(UserModel user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(UserProfile userProfile : user.getUserProfiles()){
			System.out.println("UserProfile : "+userProfile);
			authorities.add(new SimpleGrantedAuthority("ROLE_"+userProfile.getType()));
		}
		System.out.print("authorities :"+authorities);
		return authorities;
	}
	
}
