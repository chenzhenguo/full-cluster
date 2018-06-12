package com.hhcf.learn.dao;

import com.hhcf.learn.model.UserModel;

/**
 * @Title: UserMapper
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:38:25
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 */
public interface UserMapper {
	/**
	 * 根据用户名查找
	 * 
	 * @param userName
	 * @return
	 */
	public UserModel findUserByUserName(String name);

}
