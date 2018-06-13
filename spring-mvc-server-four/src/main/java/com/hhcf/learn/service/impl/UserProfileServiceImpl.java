package com.hhcf.learn.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.learn.model.UserProfileModel;
import com.hhcf.learn.service.UserProfileService;

/**
 * @Title: UserProfileServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 下午2:49:40
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate.html}
 */
@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

	@Override
	public List<UserProfileModel> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserProfileModel findByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserProfileModel findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
