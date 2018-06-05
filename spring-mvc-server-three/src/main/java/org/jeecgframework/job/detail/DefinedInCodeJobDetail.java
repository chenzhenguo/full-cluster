package org.jeecgframework.job.detail;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Title: DefinedInCodeJobDetail
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月5日 上午9:32:17
 * @see {@linkplain https://www.cnblogs.com/SpaceAnt/p/6354446.html}
 */
public class DefinedInCodeJobDetail extends QuartzJobBean {

	/**
	 * 定时作业-业务方法
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时作业2，当前时间是：" + System.currentTimeMillis());
	}

}
