package com.hhcf.study.storm.bolt;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 * 
 * @Title: WsBolt
 * @Description:
 * @Author: zhaotf
 * @Since:2018年8月30日 下午2:21:09
 */
public class WsBolt extends BaseRichBolt {

	private Map stormConf;
	private TopologyContext context;
	private OutputCollector collector;
	int sum = 0;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		// TODO Auto-generated method stub
		this.stormConf = stormConf;
		this.context = context;
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
//		1、从上游接收数据
		int hmorder = input.getIntegerByField("hmorder");
//		2、累加数据
		sum += hmorder;
//		3、实时显示累加数据
		System.out.println("bolt-sum-------"+sum);
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
