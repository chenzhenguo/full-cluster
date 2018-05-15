package org.study.rate.limiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @Title: Demo1
 * @Description:有很多个任务，但希望每秒不超过X个，可用此类
 * @Author: zhaotf
 * @Since:2018年3月30日 下午4:02:47
 * @see {@linkplain https://www.cnblogs.com/yeyinfu/p/7316972.html}
 */
public class Demo1 {
	public static void main(String[] args) {
		// 0.5代表一秒最多多少个
		RateLimiter rateLimiter = RateLimiter.create(0.5);
		List<Runnable> tasks = new ArrayList<Runnable>();
		for (int i = 0; i < 10; i++) {
			tasks.add(new UserRequest(i));
		}
		ExecutorService threadPool = Executors.newCachedThreadPool();
		for (Runnable runnable : tasks) {
			System.out.println("等待时间：" + rateLimiter.acquire());
			threadPool.execute(runnable);
		}
	}

	private static class UserRequest implements Runnable {
		private int id;

		public UserRequest(int id) {
			this.id = id;
		}

		public void run() {
			System.out.println(id);
		}
	}
}
