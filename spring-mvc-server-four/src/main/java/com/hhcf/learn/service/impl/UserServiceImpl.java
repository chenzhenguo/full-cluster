package com.hhcf.learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UserServiceImpl:" + username);
		UserModel user = null;
		try {
			user = userMapper.findUserByUserName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user == null) {
			throw new UsernameNotFoundException("用户名或密码不正确!");
		}
		System.out.println("username: " + user.getUsername());
		System.out.println("password: " + user.getPassword());

		return user;
	}

}
