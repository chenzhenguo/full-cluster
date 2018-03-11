package org.study.storm.helloworld;

import java.util.Map;
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title: TestWordSpout.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 下午12:56:25
 * @see {@linkplain https://www.cnblogs.com/hd3013779515/p/6965311.html}
 */
public class TestWordSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	public static Logger LOG = LoggerFactory.getLogger(TestWordSpout.class);
	SpoutOutputCollector _collector;

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		_collector = collector;
	}

	@Override
	public void nextTuple() {
		Utils.sleep(100);
		final String[] words = new String[] { "张三", "李四", "王五", "nathan", "mike", "jackson", "golda", "bertels" };
		final Random rand = new Random();
		final String word = words[rand.nextInt(words.length)];
		_collector.emit(new Values(word));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
