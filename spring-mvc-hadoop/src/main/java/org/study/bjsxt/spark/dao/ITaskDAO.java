package org.study.bjsxt.spark.dao;

import org.study.bjsxt.spark.domain.Task;

/**
 * 任务管理DAO接口
 * @author root
 *
 */
public interface ITaskDAO {

	/**
	 * 根据task的主键查询指定的任务
	 * @param taskId
	 * @return
	 */
	Task findTaskById(long taskId);
}
