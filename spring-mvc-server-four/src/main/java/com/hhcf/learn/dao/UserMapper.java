package com.hhcf.learn.dao;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import com.hhcf.learn.model.UserModel;

/**
 * @Title: UserMapper
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:38:25
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate.html}
 */
public interface UserMapper {
	/**
	 * 根据用户名查找
	 * 
	 * @param username
	 * @return
	 */
	public UserModel findUserByUserName(String username);

	/**
	 * 
	 * @param user
	 *            void
	 */
	// @Options(useGeneratedKeys=true,keyColumn="id")
	// @InsertProvider(method = "dynamicSQL", type = SpecialProvider)
	public void save(UserModel user);

	/**
	 * 
	 * @param id
	 * @return UserModel
	 */
	public UserModel findById(long id);

	/**
	 * 
	 * @param username
	 * @return UserModel
	 */
	public UserModel findByUserName(String username);

}
