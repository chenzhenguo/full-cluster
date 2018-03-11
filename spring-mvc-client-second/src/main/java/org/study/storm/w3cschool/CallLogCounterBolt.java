package org.study.storm.w3cschool;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

/**
 * @Title: CallLogCounterBolt.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月11日 上午8:17:55
 * @see {@linkplain https://www.w3cschool.cn/apache_storm/apache_storm_working_example.html}
 */
public class CallLogCounterBolt implements IRichBolt {
	private static final long serialVersionUID = 1L;
	Map<String, Integer> counterMap;
	private OutputCollector collector;

	@Override
	public void cleanup() {
		for (Map.Entry<String, Integer> entry : counterMap.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	@Override
	public void execute(Tuple tuple) {
		String call = tuple.getString(0);
		// Integer duration = tuple.getInteger(1);

		if (!counterMap.containsKey(call)) {
			counterMap.put(call, 1);
		} else {
			Integer c = counterMap.get(call) + 1;
			counterMap.put(call, c);
		}

		collector.ack(tuple);
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.counterMap = new HashMap<String, Integer>();
		this.collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("call"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
