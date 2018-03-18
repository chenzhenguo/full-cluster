package com.hhcf.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.backend.dao.FullMybatisDao;
import com.hhcf.backend.service.FullService;

/**
 * 
 * @Title: FullServiceImpl
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月12日 下午2:10:18
 */
@Service("fullService")
@Transactional
public class FullServiceImpl implements FullService {
	@Autowired
	private FullMybatisDao fullMybatisDao;
	
	
	
}
