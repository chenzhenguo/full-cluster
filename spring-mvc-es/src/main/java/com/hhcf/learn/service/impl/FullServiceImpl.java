package com.hhcf.learn.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.learn.dao.FullMybatisDao;
import com.hhcf.learn.model.UserModel;
import com.hhcf.learn.service.FullService;

/**
 * 
 * @Title: FullServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2017年7月11日 上午10:06:53
 * @Version:1.0
 */
@Service("fullService")
@Transactional
public class FullServiceImpl implements FullService {
	@Autowired
	private FullMybatisDao fullMybatisDao;

	@Override
	public Object findUsers(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("delete_flag", 0);
		params.put("status", Integer.valueOf(request.getParameter("status")));
		List<UserModel> list = fullMybatisDao.findUsers(params);
		return list;
	}

}
