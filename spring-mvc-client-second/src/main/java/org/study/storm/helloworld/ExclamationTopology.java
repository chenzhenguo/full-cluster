package org.study.storm.helloworld;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * @Title: ExclamationTopology.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 下午12:55:25
 * @see {@linkplain https://www.cnblogs.com/hd3013779515/p/6965311.html}
 */
public class ExclamationTopology {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("word", new TestWordSpout(), 1);
		builder.setBolt("exclaim", new ExclamationBolt(), 1).shuffleGrouping("word");
		builder.setBolt("print", new PrintBolt(), 1).shuffleGrouping("exclaim");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			// 集群模式
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			// 本地模式
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test3", conf, builder.createTopology());
			Utils.sleep(20000);
			cluster.killTopology("test3");
			cluster.shutdown();
		}
	}

}
