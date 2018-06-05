package org.jeecgframework.job.detail;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @see {@linkplain https://www.cnblogs.com/SpaceAnt/p/6354446.html}
 * @Title: DefinedInXMLJobDetail
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月5日 上午9:29:44
 */
public class DefinedInXMLJobDetail extends QuartzJobBean {

	/**
	 * 定时作业-业务方法
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("定时作业1，当前时间是：" + System.currentTimeMillis());
	}

}
