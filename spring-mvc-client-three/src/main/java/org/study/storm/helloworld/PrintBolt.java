package org.study.storm.helloworld;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: PrintBolt.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 下午1:02:11
 * @see {@linkplain https://www.cnblogs.com/hd3013779515/p/6965311.html}
 */
public class PrintBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(PrintBolt.class);
	OutputCollector _collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		LOG.info(tuple.getString(0) + " Hello World!测试二层");
		_collector.emit(tuple, new Values(tuple.getString(0) + "!!!PrintBolt二层"));
		_collector.ack(tuple);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("exclaim"));
	}

}
