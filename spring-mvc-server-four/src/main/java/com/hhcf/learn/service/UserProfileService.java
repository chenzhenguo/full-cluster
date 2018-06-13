package com.hhcf.learn.service;

import java.util.List;

import com.hhcf.learn.model.UserProfileModel;

/**
 * @Title: UserProfileService
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 下午2:49:17
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate.html}
 */
public interface UserProfileService {

	public List<UserProfileModel> findAll();

	public UserProfileModel findByType(String type);

	public UserProfileModel findById(int id);

}
