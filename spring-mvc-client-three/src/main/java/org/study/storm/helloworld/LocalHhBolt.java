package org.study.storm.helloworld;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

/**
 * @Title: LocalHhBolt.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 下午4:20:31
 * @see {@linkplain https://www.cnblogs.com/hd3013779515/p/6965311.html}
 */
public class LocalHhBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		System.out.println("aaa:"+input.getString(0));
		_collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("hmlc"));
	}

}
