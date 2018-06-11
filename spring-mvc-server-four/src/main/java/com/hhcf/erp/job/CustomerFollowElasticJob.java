package com.hhcf.erp.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * @Title: CustomerFollowElasticJob
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月11日 上午11:13:06
 * @see {@linkplain https://blog.csdn.net/itlqi/article/details/77973266}
 */
public class CustomerFollowElasticJob implements SimpleJob {

	@Override
	public void execute(ShardingContext context) {
		System.out.println("elastic-job:" + context.getJobName() + "," + context.getJobParameter() + ","
				+ context.getTaskId() + "," + context.getShardingParameter());
		testShareData(context.getShardingItem(), context.getShardingTotalCount(), 100, context.getTaskId());
	}

	/**
	 * @Description: 测试分片数据 参数一当前分片 参数二分片总数 参数三 总条数
	 * @return void
	 * @date 2017年9月7日 下午5:24:41
	 */
	public void testShareData(int sharItem, int sharCount, int dataCount, String taskId) {
		if (sharItem == (sharCount - 1)) {// 如果总分片数据==当前分片 说明是最后数据
			System.out.println("分片:" + taskId + "," + sharItem + ",执行数据:" + ((dataCount / sharCount) * sharItem) + " 到 "
					+ ((dataCount / sharCount) * (sharItem == 0 ? 1 : sharItem + 1) + (dataCount % sharCount)));
		} else {
			System.out.println("分片:" + taskId + "," + sharItem + ",执行数据:" + ((dataCount / sharCount) * sharItem) + " 到 "
					+ (dataCount / sharCount) * (sharItem == 0 ? 1 : sharItem + 1));
		}
	}
}
