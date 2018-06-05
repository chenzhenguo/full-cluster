package com.hhcf.backend.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hhcf.backend.dao.TimeTaskDao;
import com.hhcf.backend.entity.TSTimeTaskEntity;
import com.hhcf.system.dao.impl.HibernateBaseDaoImpl;

/**
 * @Title: TimeTaskDaoImpl
 * @Description:定时任务管理
 * @Author: zhaotf
 * @Since:2018年6月5日 下午2:11:58
 */
@Repository("timeTaskDao")
public class TimeTaskDaoImpl extends HibernateBaseDaoImpl implements TimeTaskDao {
	@Override
	public TSTimeTaskEntity getObjTask(String taskId) {
		Criteria c = getCurrentSession().createCriteria(TSTimeTaskEntity.class);
		c.add(Restrictions.eq("taskId", taskId));
//		c.add(Restrictions.eq("deleteflag", "0"));
		return (TSTimeTaskEntity) c.uniqueResult();
	}

	@Override
	public void updateTask(TSTimeTaskEntity task) {
		// TODO Auto-generated method stub

	}

	@Override
	public TSTimeTaskEntity getTask(String id) {
		Criteria c = getCurrentSession().createCriteria(TSTimeTaskEntity.class);
		c.add(Restrictions.eq("id", id));
//		c.add(Restrictions.eq("deleteflag", "0"));
		return (TSTimeTaskEntity) c.uniqueResult();
	}

	@Override
	public List<TSTimeTaskEntity> findTaskList(TSTimeTaskEntity timTask) {
		Criteria c = getCurrentSession().createCriteria(TSTimeTaskEntity.class);
		c.add(Restrictions.eq("isEffect", timTask.getIsEffect()));
		c.add(Restrictions.eq("isStart", timTask.getIsStart()));
//		c.add(Restrictions.eq("deleteflag", "0"));
		return c.list();
	}
}
