package org.study.hbase.index;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequestBuilder;

/**
 * @Title: ElasticSearchBulkOperator.java
 * @Description:使用Hbase协作器(Coprocessor)同步数据到ElasticSearch
 * @see 最后就是比较关键的bulk ES代码，结合2shou的代码，我自己写的这部分代码，没有使用Timer，
 *      而是使用了ScheduledExecutorService，至于为什么不使用Timer，
 *      大家可以去百度上面搜索下这两个东东的区别，我在这里就不做过多的介绍了。 在ElasticSearchBulkOperator这个类中，
 *      我使用ScheduledExecutorService周期性的执行一个任务，去判断缓冲池中，是否有需要bulk的数据，
 *      阀值是10000.每30秒执行一次，如果达到阀值，那么就会立即将缓冲池中的数据bulk到ES中，并清空缓冲池中的数据，
 *      等待下一次定时任务的执行。当然，初始化定时任务需要一个beeper响铃的线程，delay时间10秒。
 *      还有一个很重要的就是需要对bulk的过程进行加锁操作。
 * @author zhaotf
 * @date 2018年3月4日 下午1:40:44
 * @see {@linkplain http://blog.csdn.net/fxsdbt520/article/details/53884338}
 */
public class ElasticSearchBulkOperator {
	private static final Log LOG = LogFactory.getLog(ElasticSearchBulkOperator.class);

	private static final int MAX_BULK_COUNT = 10000;// 缓冲池阈值
	private static BulkRequestBuilder bulkRequestBuilder = null;
	private static final Lock commitLock = new ReentrantLock();
	private static ScheduledExecutorService scheduledExecutorService = null;// 定时任务

	static {
		// init es bulkRequestBuilder
		bulkRequestBuilder = ESClient.client.prepareBulk();
		bulkRequestBuilder.setRefreshPolicy(RefreshPolicy.IMMEDIATE);

		// init thread pool and set size 1
		scheduledExecutorService = Executors.newScheduledThreadPool(1);

		// create beeper thread( it will be sync data to ES cluster)
		// use a commitLock to protected bulk es as thread-save
		final Runnable beeper = new Runnable() {
			public void run() {
				commitLock.lock();
				try {
					bulkRequest(0);
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					LOG.error("Time Bulk " + ESClient.indexName + " index error : " + ex.getMessage());
				} finally {
					commitLock.unlock();
				}
			}
		};

		// set time bulk task
		// set beeper thread(10 second to delay first execution , 30 second
		// period between successive executions)
		scheduledExecutorService.scheduleAtFixedRate(beeper, 10, 30, TimeUnit.SECONDS);

	}

	/**
	 * shutdown time task immediately
	 */
	public static void shutdownScheduEx() {
		if (null != scheduledExecutorService && !scheduledExecutorService.isShutdown()) {
			scheduledExecutorService.shutdown();
		}
	}

	/**
	 * bulk request when number of builders is grate then threshold
	 *
	 * @param threshold
	 */
	private static void bulkRequest(int threshold) {
		if (bulkRequestBuilder.numberOfActions() > threshold) {
			BulkResponse bulkItemResponse = bulkRequestBuilder.execute().actionGet();
			if (!bulkItemResponse.hasFailures()) {
				bulkRequestBuilder = ESClient.client.prepareBulk();
			}
		}
	}

	/**
	 * add update builder to bulk use commitLock to protected bulk as
	 * thread-save
	 * 
	 * @param builder
	 */
	public static void addUpdateBuilderToBulk(UpdateRequestBuilder builder) {
		commitLock.lock();
		try {
			bulkRequestBuilder.add(builder);
			bulkRequest(MAX_BULK_COUNT);
		} catch (Exception ex) {
			LOG.error(" update Bulk " + ESClient.indexName + " index error : " + ex.getMessage());
		} finally {
			commitLock.unlock();
		}
	}

	/**
	 * add delete builder to bulk use commitLock to protected bulk as
	 * thread-save
	 *
	 * @param builder
	 */
	public static void addDeleteBuilderToBulk(DeleteRequestBuilder builder) {
		commitLock.lock();
		try {
			bulkRequestBuilder.add(builder);
			bulkRequest(MAX_BULK_COUNT);
		} catch (Exception ex) {
			LOG.error(" delete Bulk " + ESClient.indexName + " index error : " + ex.getMessage());
		} finally {
			commitLock.unlock();
		}
	}

}
