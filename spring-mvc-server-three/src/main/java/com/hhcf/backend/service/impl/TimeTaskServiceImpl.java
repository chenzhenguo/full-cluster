package com.hhcf.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhcf.backend.dao.TimeTaskDao;
import com.hhcf.backend.entity.TSTimeTaskEntity;
import com.hhcf.backend.service.TimeTaskService;

/**
 * 
 * @Title: TimeTaskServiceImpl
 * @Description:定时任务管理
 * @Author: zhaotf
 * @Since:2018年6月5日 上午11:24:39
 */
@Service("timeTaskService")
@Transactional
public class TimeTaskServiceImpl implements TimeTaskService {
	@Autowired
	private TimeTaskDao timeTaskDao;

	@Override
	public TSTimeTaskEntity getObjTask(String taskId) {
		return timeTaskDao.getObjTask(taskId);
	}

	@Override
	public void updateTask(TSTimeTaskEntity task) {
		timeTaskDao.saveOrUpdate(task);
	}

	@Override
	public TSTimeTaskEntity getTask(String id) {
		return timeTaskDao.getTask(id);
	}

	@Override
	public List<TSTimeTaskEntity> findTaskList(TSTimeTaskEntity timTask) {
		return timeTaskDao.findTaskList(timTask);
	}

}
