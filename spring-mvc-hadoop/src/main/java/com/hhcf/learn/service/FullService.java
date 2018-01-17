package com.hhcf.learn.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @Title: FullService
 * @Description:
 * @Author: zhaotf
 * @Since:2017年7月11日 上午10:06:29
 * @Version:1.0
 */
public interface FullService {
	/**
	 * 数据列表查询
	 * 
	 * @param request
	 * @return Object
	 */
	public Object findUsers(HttpServletRequest request);

	/**
	 */
	public Object testConnect(HttpServletRequest request);

}
