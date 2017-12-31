package com.hhcf.learn.dao;

import java.util.List;
import java.util.Map;

import com.hhcf.learn.model.UserModel;

/**
 * 
 * @Title: FullMybatisDao
 * @Description:
 * @Author: zhaotf
 * @Since:2017年7月11日 上午10:09:16
 * @Version:1.0
 */
public interface FullMybatisDao {

	/**
	 * 列表数据查询
	 * 
	 * @param params
	 * @return List<UserModel>
	 */
	public List<UserModel> findUsers(Map<String, Object> params);

}
