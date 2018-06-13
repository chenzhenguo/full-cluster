package com.hhcf.learn.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.hhcf.learn.model.UserModel;

/**
 * @Title: UserService
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:44:47
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 */
public interface UserService extends UserDetailsService {

	/**
	 * 
	 * @param user
	 *            void
	 */
	public void save(UserModel user);

	/**
	 * 
	 * @param id
	 * @return UserModel
	 */
	public UserModel findById(long id);

	/**
	 * 
	 * @param userName
	 * @return UserModel
	 */
	public UserModel findByUserName(String userName);

}
