package com.hhcf.study.storm.spouts;

import java.util.List;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * 
 * @Title: WsSpout
 * @Description:
 * @Author: zhaotf
 * @Since:2018年8月30日 下午2:07:55
 */
public class WsSpout extends BaseRichSpout {
	private Map map;
	private TopologyContext context;
	private SpoutOutputCollector collector;
	int i = 0;

	/**
	 * 向下发送数据，传给下一节点，无限循环
	 */
	@Override
	public void nextTuple() {
		i++;
		// 使用collector发送数据
		List tuple = new Values(i);
		this.collector.emit(tuple);
		System.err.println("spout发送----" + tuple);
		try {
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 接收数据
	 */
	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		this.map = map;
		this.context = context;
		this.collector = collector;

	}

	/**
	 * 声明向下游发送数据的类型
	 * 
	 * @param declare
	 *            void
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declare) {
		declare.declare(new Fields("hmorder"));
	}

}
