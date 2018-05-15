package org.study.storm.helloworld;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @Title: ExclamationBolt.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 下午1:00:10
 * @see {@linkplain https://www.cnblogs.com/hd3013779515/p/6965311.html}
 */
public class ExclamationBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		System.out.println(tuple.getString(0) + " Hello World!测试一层");
		_collector.emit(tuple, new Values(tuple.getString(0) + "!!!ExclamationBolt一层"));
		_collector.ack(tuple);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
