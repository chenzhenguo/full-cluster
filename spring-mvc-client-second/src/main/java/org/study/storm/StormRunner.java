package org.study.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;

/**
 * @Title: StormRunner
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 下午3:04:49
 * @see {@linkplain https://www.cnblogs.com/jietang/p/5423438.html}
 */
public class StormRunner {

	private static final int MILLIS_IN_SEC = 1000;

	// 本地拓扑 Storm用一个进程里面的N个线程进行模拟
	public static void runTopologyLocally(StormTopology topology, String topologyName, Config conf,
			int runtimeInSeconds) throws InterruptedException {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(topologyName, conf, topology);
		Thread.sleep((long) runtimeInSeconds * MILLIS_IN_SEC);
		cluster.killTopology(topologyName);
		cluster.shutdown();
	}

	// 分布式拓扑 真正的Storm集群运行环境
	public static void runTopologyRemotely(StormTopology topology, String topologyName, Config conf)
			throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
		StormSubmitter.submitTopology(topologyName, conf, topology);
	}

}
