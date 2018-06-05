package com.hhcf.backend.service;

import java.util.List;

import com.hhcf.backend.entity.TSTimeTaskEntity;

/**
 * 
 * @Title: TimeTaskService
 * @Description:定时任务管理
 * @Author: zhaotf
 * @Since:2018年6月5日 上午11:24:08
 */
public interface TimeTaskService {

	/**
	 * 获取定时作业对象
	 * 
	 * @param taskId
	 * @return TSTimeTaskEntity
	 */
	public TSTimeTaskEntity getObjTask(String taskId);

	/**
	 * 更新定时任务
	 * 
	 * @param task
	 *            void
	 */
	public void updateTask(TSTimeTaskEntity task);

	/**
	 * 获取定时作业对象
	 * 
	 * @param id
	 * @return TSTimeTaskEntity
	 */
	public TSTimeTaskEntity getTask(String id);

	/**
	 * 
	 * @param timTask
	 * @return List<TSTimeTaskEntity>
	 */
	public List<TSTimeTaskEntity> findTaskList(TSTimeTaskEntity timTask);

}
