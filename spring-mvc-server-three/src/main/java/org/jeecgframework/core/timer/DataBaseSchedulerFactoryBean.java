package org.jeecgframework.core.timer;

import java.util.Set;

import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.hhcf.backend.entity.TSTimeTaskEntity;
import com.hhcf.backend.service.TimeTaskService;

/**
 * 
 * @Title: DataBaseSchedulerFactoryBean
 * @Description:调度工厂类，读取数据库 然后判断是否启动任务
 * @Author: zhaotf
 * @Since:2018年6月5日 上午11:22:12
 */
public class DataBaseSchedulerFactoryBean extends SchedulerFactoryBean {
	@Autowired
	private TimeTaskService timeTaskService;

	/**
	 * 读取数据库判断是否开始定时任务
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		// String[] trigerrNames =
		// this.getScheduler().getTriggerNames(Scheduler.DEFAULT_GROUP);
		// TriggerKey tk = TriggerKey.triggerKey(Scheduler.DEFAULT_GROUP);
		Set<TriggerKey> trigerrKeys = this.getScheduler()
				.getTriggerKeys(GroupMatcher.triggerGroupContains(Scheduler.DEFAULT_GROUP));

		TSTimeTaskEntity task;

		for (TriggerKey tk : trigerrKeys) {
			task = timeTaskService.getObjTask(tk.getName());
			// 数据库查询不到的定时任务或者定时任务的运行状态不为1时，都停止
			if (task == null || !"1".equals(task.getIsStart())) {
				// this.getScheduler().pauseTrigger(tk.getName(),
				// Scheduler.DEFAULT_GROUP);
				this.getScheduler().pauseTrigger(tk);
			}
		}
	}

}
