package org.study.thred;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: CustomThreadPoolExecutor
 * @Description:
 * @author: zhaotf
 * @date: 2018年9月27日 下午3:37:53
 * @see {@linkplain https://www.cnblogs.com/zedosu/p/6665306.html}
 */
public class CustomThreadPoolExecutor {

	private ThreadPoolExecutor pool = null;

	/**
	 * 线程池初始化方法
	 * 
	 * corePoolSize 核心线程池大小----1 maximumPoolSize 最大线程池大小----3 keepAliveTime
	 * 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit TimeUnit
	 * keepAliveTime时间单位----TimeUnit.MINUTES workQueue 阻塞队列----new
	 * ArrayBlockingQueue<Runnable>(5)====5容量的阻塞队列 threadFactory 新建线程工厂----new
	 * CustomThreadFactory()====定制的线程工厂 rejectedExecutionHandler
	 * 当提交任务数超过maxmumPoolSize+workQueue之和时,
	 * 即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
	 * 任务会交给RejectedExecutionHandler来处理
	 */
	public void init() {
		// pool = new ThreadPoolExecutor(1, 3, 30, TimeUnit.MINUTES, new
		// ArrayBlockingQueue<Runnable>(5),
		// new CustomThreadFactory(), new CustomRejectedExecutionHandler());
		pool = new ThreadPoolExecutor(10, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void destory() {
		if (pool != null) {
			pool.shutdownNow();
		}
	}

	public ExecutorService getCustomThreadPoolExecutor() {
		return this.pool;
	}

	private class CustomThreadFactory implements ThreadFactory {

		private AtomicInteger count = new AtomicInteger(0);

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			String threadName = CustomThreadPoolExecutor.class.getSimpleName() + count.addAndGet(1);
			System.out.println(threadName);
			t.setName(threadName);
			return t;
		}
	}

	private class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			try {
				// 核心改造点，由blockingqueue的offer改成put阻塞方法
				executor.getQueue().put(r);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 测试构造的线程池
	public static void main(String[] args) {

		final CustomThreadPoolExecutor exec = new CustomThreadPoolExecutor();
//		// 1.初始化
//		exec.init();
//		ExecutorService pool = exec.getCustomThreadPoolExecutor();
		ExecutorService pool = new ThreadPoolExecutor(10, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
				new ThreadPoolExecutor.CallerRunsPolicy());

		for (int i = 0; i < 2; i++) {
			String name = "zhaotf" + i;
			System.out.println("提交第" + i + "个任务!");
			
			pool.execute(exec.new TestRunnable(name));
//			pool.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						System.out.println(">>>task is running=====");
//						TimeUnit.SECONDS.sleep(10);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			});
		}

		// 2.销毁----此处不能销毁,因为任务没有提交执行完,如果销毁线程池,任务也就无法执行了
		// exec.destory();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	class TestRunnable implements Runnable {
		private String name;

		public TestRunnable(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				System.out.println("子线程:" + name + "," + Thread.currentThread().getId());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	class TestCallable implements Callable<String> {

		private String name;

		public TestCallable(String name) {
			this.name = name;
		}

		@Override
		public String call() throws Exception {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return this.name;
		}
	}
}
