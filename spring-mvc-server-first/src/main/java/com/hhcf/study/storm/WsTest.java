package com.hhcf.study.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import com.hhcf.study.storm.bolt.WsBolt;
import com.hhcf.study.storm.spouts.WsSpout;

/**
 * 
 * @Title: WsTest
 * @Description:
 * @Author: zhaotf
 * @Since:2018年8月30日 下午2:40:11
 */
public class WsTest {

	public static void main(String[] args) {
		// 通过构建者，构建当前的topology作业
		TopologyBuilder tb = new TopologyBuilder();
		tb.setSpout("wsSpout", new WsSpout());
		tb.setBolt("wsBolt", new WsBolt()).shuffleGrouping("wsSpout");

		// 创建本地模拟支行环境
		LocalCluster ls = new LocalCluster();
		ls.submitTopology("wordsum", new Config(), tb.createTopology());

	}

}
