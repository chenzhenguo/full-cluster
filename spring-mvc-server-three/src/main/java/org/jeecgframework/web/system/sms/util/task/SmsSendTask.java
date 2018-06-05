package org.jeecgframework.web.system.sms.util.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 
 * @Title: SmsSendTask
 * @Description:消息推送定时任务
 * @Author: zhaotf
 * @Since:2018年6月5日 下午2:27:57
 */
@Component("smsSendTask")
public class SmsSendTask implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long start = System.currentTimeMillis();
		org.jeecgframework.core.util.LogUtil.info("===================推送消息定时任务开始===================");
		try {
			// tSSmsService.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		org.jeecgframework.core.util.LogUtil.info("===================推送消息定时任务结束===================");
		long end = System.currentTimeMillis();
		long times = end - start;
		org.jeecgframework.core.util.LogUtil.info("总耗时" + times + "毫秒");
	}

}
