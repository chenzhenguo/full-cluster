package com.hhcf.study.storm.bolt;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

/**
 * @Title: WordCounter
 * @Description:
 * @Author: zhaotf
 * @Since:2018年8月30日 下午4:19:21
 * @see {@linkplain https://blog.csdn.net/qq_37095882/article/details/77624246}
 */
public class WordCounter implements IRichBolt {

	private String name;
	private Integer id;
	Map<String, Integer> counters;
	private OutputCollector collector;

	/**
	 * 初始化
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}

	/**
	 * 为每个单词计数
	 */
	@Override
	public void execute(Tuple input) {
		String str = input.getString(0);
		System.out.println("ccccccccccc");
//		String str = input.getStringByField("word");
		System.out.println("dddddddddddd");
		/**
		 * 如果单词尚不存在于map，我们就创建一个，如果已在，我们就为它加1
		 */
		if (!counters.containsKey(str)) {
			counters.put(str, 1);
		} else {
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
		// 对元组作为应答
		collector.ack(input);
	}

	/**
	 * 这个spout结束时（集群关闭的时候），我们会显示单词数量
	 */
	@Override
	public void cleanup() {
		System.out.println("-- 单词数 【" + name + "-" + id + "】 --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			System.out.println("遍历:" + entry.getKey() + ": " + entry.getValue());
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
