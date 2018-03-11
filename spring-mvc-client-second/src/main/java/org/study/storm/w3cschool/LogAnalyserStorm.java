package org.study.storm.w3cschool;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * @Title: LogAnalyserStorm.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 上午8:21:32
 * @see {@linkplain https://www.w3cschool.cn/apache_storm/apache_storm_working_example.html}
 */
public class LogAnalyserStorm {

	public static void main(String[] args) throws Exception {
		// Create Config instance for cluster configuration
		Config config = new Config();
		config.setDebug(true);

		//
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("call-log-reader-spout", new FakeCallLogReaderSpout());

		builder.setBolt("call-log-creator-bolt", new CallLogCreatorBolt()).shuffleGrouping("call-log-reader-spout");

		builder.setBolt("call-log-counter-bolt", new CallLogCounterBolt()).fieldsGrouping("call-log-creator-bolt",
				new Fields("call"));

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("LogAnalyserStorm", config, builder.createTopology());
		Thread.sleep(10000);

		// Stop the topology

		cluster.shutdown();
	}

}
