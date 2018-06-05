package org.jeecgframework.util;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.job.detail.DefinedInCodeJobDetail;
import org.jeecgframework.job.detail.DefinedInXMLJobDetail;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Title: QuartzUtil
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月5日 上午9:34:01
 * @see {@linkplain https://www.cnblogs.com/SpaceAnt/p/6354446.html}
 */
@Component("quartzUtil")
public class QuartzUtil {

	@Resource(name = "quartzScheduler")
	private Scheduler scheduler;

	private static String JOB_GROUP_NAME = "ddlib";
	private static String TRIGGER_GROUP_NAME = "ddlibTrigger";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param job
	 *            Job任务类实例
	 * @param jobName
	 *            job名字(请保证唯一性)
	 * @param cronExpression
	 *            cron时间表达式
	 * @throws SchedulerException
	 */
	public void addJob(String jobName, Job job, String cronExpression) throws SchedulerException {
		addJob(job, jobName, null, jobName, null, cronExpression, 5);
	}

	/**
	 * 开始一个simpleSchedule()调度(创建一个新的定时任务)
	 * 
	 * @param job
	 *            Job任务类实例
	 * @param jobName
	 *            job名字(请保证唯一性)
	 * @param jobGroupName
	 *            job组名(请保证唯一性)
	 * @param cronExpression
	 *            cron时间表达式
	 * @param triggerName
	 *            trigger名字(请保证唯一性)
	 * @param triggerGroupName
	 *            triggerjob组名(请保证唯一性)
	 * @throws SchedulerException
	 */
	public void addJob(Job job, String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			String cronExpression, int triggerPriority) throws SchedulerException {

		if (StringUtils.isEmpty(jobGroupName)) {
			jobGroupName = JOB_GROUP_NAME;
		}
		if (StringUtils.isEmpty(triggerGroupName)) {
			triggerGroupName = TRIGGER_GROUP_NAME;
		}

		// 1、创建一个JobDetail实例，指定Quartz
		JobDetail jobDetail = JobBuilder.newJob(job.getClass()) // 任务执行类
				.withIdentity(jobName, jobGroupName) // 任务名，任务组
				.build();

		// 2、创建Trigger
		// 优先级默认是5，数字越高优先级越高
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName).startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).withPriority(triggerPriority).build();

		// 3、移除job，避免因为job的重复添加导致错误
		this.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);

		// 4、调度执行
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}

		// 4、启动
		this.startSchedule();
	}

	/**
	 * 开始任务
	 * 
	 * @throws SchedulerException
	 */
	public void startSchedule() throws SchedulerException {
		try {
			if (scheduler.isShutdown()) {
				scheduler.resumeAll();
			} else {
				scheduler.start();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 暂停Job
	 * 
	 * @param name
	 *            job名字
	 * @param group
	 *            job组名
	 * @throws SchedulerException
	 */
	public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 恢复Job
	 * 
	 * @param name
	 *            job名字
	 * @param group
	 *            job组名
	 * @throws SchedulerException
	 */
	public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 */
	public void modifyJobTime(String jobName, String cronExpression) throws SchedulerException {
		rescheduleJob(jobName, null, cronExpression);
	}

	/**
	 * 更新任务表达式
	 * 
	 * @param triggerName
	 *            trigger名字
	 * @param triggerGroupName
	 *            trigger组名
	 * @param newCronExpression
	 *            cron时间表达式
	 * @throws SchedulerException
	 */
	public void rescheduleJob(String triggerName, String triggerGroupName, String newCronExpression)
			throws SchedulerException {

		if (StringUtils.isBlank(triggerGroupName)) {
			triggerGroupName = TRIGGER_GROUP_NAME;
		}

		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

		// 获取trigger
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 按新的cronExpression表达式重新构建trigger
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
				.withSchedule(CronScheduleBuilder.cronSchedule(newCronExpression)).build();

		// 按新的trigger重新设置job执行
		try {
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/** 移除一个任务和触发器(使用默认的任务组名，触发器名，触发器组名) */
	public void removeJob(String jobName, String triggerName) throws SchedulerException {
		removeJob(jobName, null, triggerName, null);
	}

	/** 移除一个任务和触发器 */
	public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
			throws SchedulerException {

		if (StringUtils.isEmpty(jobGroupName)) {
			jobGroupName = JOB_GROUP_NAME;
		}
		if (StringUtils.isEmpty(triggerGroupName)) {
			triggerGroupName = TRIGGER_GROUP_NAME;
		}

		JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

		scheduler.pauseTrigger(triggerKey); // 停止触发器
		scheduler.unscheduleJob(triggerKey); // 移除触发器
		scheduler.deleteJob(jobKey); // 删除任务
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-mvc.xml",
				"classpath:spring-mvc-hibernate.xml", "classpath:spring-mvc-quartz.xml");

		QuartzUtil quartzUtil = (QuartzUtil) context.getBean("quartzUtil");

		// 删除定时器
		try {
			quartzUtil.removeJob("jobName", "jobGroupName", "triggerName", "triggerGroupName");
			quartzUtil.removeJob("jobName1", "jobGroupName", "triggerName", "triggerGroupName");
			quartzUtil.removeJob("jobName2", "jobGroupName", "triggerName", "triggerGroupName");
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}

		// 添加定时器
		try {
			quartzUtil.addJob(new DefinedInXMLJobDetail(), "jobName1", "jobGroupName1", "triggerName1",
					"triggerGroupName1", "0/20 * * * * ? ", 5);

			quartzUtil.addJob(new DefinedInCodeJobDetail(), "jobName2", "jobGroupName2", "triggerName2",
					"triggerGroupName2", "0/10 * * * * ? ", 5);

		} catch (SchedulerException e2) {
			e2.printStackTrace();
		}

		// 修改定时器
		try {
			quartzUtil.rescheduleJob("triggerName1", "triggerGroupName1", "0/30 * * * * ? ");
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.sleep(100 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
