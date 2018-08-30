package com.hhcf.study.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import com.hhcf.study.storm.bolt.WordCounter;
import com.hhcf.study.storm.bolt.WordNormalizer;
import com.hhcf.study.storm.spouts.WordReader;

/**
 * @Title: TopologyMain
 * @Description:
 * @Author: zhaotf
 * @Since:2018年8月30日 下午4:27:56
 * @see {@linkplain https://blog.csdn.net/qq_37095882/article/details/77624246}
 */
public class TopologyMain {

	public static void main(String[] args) {
		// 定义拓扑
		TopologyBuilder tb = new TopologyBuilder();
		tb.setSpout("word-reader", new WordReader());
		tb.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		tb.setBolt("word-counter", new WordCounter(), 2).fieldsGrouping("word-normalizer", new Fields("word"));

		// 配置
		Config conf = new Config();
		conf.put("wordsFile", args[0]);
		System.out.println("启动参数:" + conf.get("wordsFile") + "," + args[0]);
		conf.setDebug(false);

		// 运行拓扑
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
		LocalCluster ls = new LocalCluster();
		ls.submitTopology("Getting-Started-Topologie", conf, tb.createTopology());

		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ls.shutdown();// 停止

	}

}
