package org.study.thred;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

/**
 * @ClassName: ThredTest
 * @Description:多线程测试
 * @author: zhaotf
 * @date: 2018年9月27日 下午2:42:09
 * @see {@linkplain https://segmentfault.com/a/1190000015368896}
 */
public class ThredTest {

	public static void main(String[] args) {
		ThredTest ts = new ThredTest();
		try {
			// ts.test4();
			ts.test3();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// test.java
	volatile int finishState = 0;
	Object lock = new Object();

	public void test3() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 200, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
		ExecutorCompletionService<String> executorCompletionService = new ExecutorCompletionService(threadPoolExecutor);

		Runnable run = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					String name = "name_" + i;
					threadPoolExecutor.execute(new TestRunnable(name));
					// threadPoolExecutor.submit(new TestCallable(name));
					// executorCompletionService.submit(new TestCallable(name));
					System.out.print("满标放款操作:" + name + ",活动线程数:" + threadPoolExecutor.getActiveCount() + ",线程池数:"
							+ threadPoolExecutor.getPoolSize() + ",核心线程数:" + threadPoolExecutor.getCorePoolSize()
							+ ",队列任务数:" + threadPoolExecutor.getQueue().size() + ",完成执行的近似任务总数:"
							+ threadPoolExecutor.getCompletedTaskCount() + ",任务数:" + threadPoolExecutor.getTaskCount());
				}

			}
		};
		Thread th = new Thread(run);
		th.start();
		System.out.println("主线程结束……");

		// threadPoolExecutor.shutdown();
		// try {
		// while (!threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
		// System.out.println("complete");
		// }
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	public void test4() throws InterruptedException, ExecutionException {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 200, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
		// ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 7,
		// 10, TimeUnit.SECONDS,
		// new LinkedBlockingDeque<>(10));
		ExecutorCompletionService<String> executorCompletionService = new ExecutorCompletionService(threadPoolExecutor);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					String name = "name_" + i;
					TestCallable testCallable = new TestCallable(name);
					try {
						executorCompletionService.submit(testCallable);

						synchronized (lock) {
							// System.out.print("+++添加任务 name: " + name);
							System.out.print("满标放款操作:" + name + ",活动线程数:" + threadPoolExecutor.getActiveCount()
									+ ",线程池数:" + threadPoolExecutor.getPoolSize() + ",核心线程数:"
									+ threadPoolExecutor.getCorePoolSize() + ",队列任务数:"
									+ threadPoolExecutor.getQueue().size() + ",完成执行的近似任务总数:"
									+ threadPoolExecutor.getCompletedTaskCount() + ",任务数:"
									+ threadPoolExecutor.getTaskCount());
						}
					} catch (RejectedExecutionException e) {
						synchronized (lock) {
							System.out.println("拒绝：" + name);
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				finishState = 1;
			}
		};

		Thread addThread = new Thread(runnable);
		addThread.start();

		// System.out.println(" taskCount: " +
		// threadPoolExecutor.getTaskCount());

		// 添加的任务有被抛弃的。taskCount不一定等于添加的任务。
		int completeCount = 0;
		while (!(completeCount == threadPoolExecutor.getTaskCount() && finishState == 1)) {
			Future<String> take = executorCompletionService.take();
			String taskName = take.get();
			synchronized (lock) {
				System.out.print("---完成任务 name: " + taskName);
				System.out.print(" ActiveCount: " + threadPoolExecutor.getActiveCount());
				System.out.print(" poolSize: " + threadPoolExecutor.getPoolSize());
				System.out.print(" queueSize: " + threadPoolExecutor.getQueue().size());
				System.out.print(" taskCount: " + threadPoolExecutor.getTaskCount());
				System.out.println(" finishTask：" + (++completeCount));

			}
		}

		addThread.join();

		while (threadPoolExecutor.getPoolSize() > 0) {
			Thread.sleep(1000);
			synchronized (lock) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
				System.out.print(simpleDateFormat.format(new Date()));
				// System.out.print("name: " + taskName);
				System.out.print(" ActiveCount: " + threadPoolExecutor.getActiveCount());
				System.out.print(" poolSize: " + threadPoolExecutor.getPoolSize());
				System.out.print(" queueSize: " + threadPoolExecutor.getQueue().size());
				System.out.println(" taskCount: " + threadPoolExecutor.getTaskCount());
			}
		}

		// Tell threads to finish off.
		threadPoolExecutor.shutdown();
		// Wait for everything to finish.
		while (!threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
			System.out.println("complete");
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
				System.out.println("complete:" + name + "," + Thread.currentThread().getId());
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
