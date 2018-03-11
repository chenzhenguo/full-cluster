package org.study.storm.w3cschool;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @Title: CallLogCreatorBolt.java
 * @Description: TODO 呼叫日志创建者bolt接收呼叫日志元组。呼叫日志元组具有主叫方号码，接收方号码和呼叫持续时间。
 *               此bolt通过组合主叫方号码和接收方号码简单地创建一个新值。新值的格式为“来电号码 -
 *               接收方号码”，并将其命名为新字段“呼叫”。
 * @author zhaotf
 * @date 2018年3月11日 上午8:14:10
 * @see {@linkplain https://www.w3cschool.cn/apache_storm/apache_storm_working_example.html}
 */
public class CallLogCreatorBolt implements IRichBolt {
	private static final long serialVersionUID = 1L;
	// Create instance for OutputCollector which collects and emits tuples to
	// produce output
	private OutputCollector collector;

	@Override
	public void cleanup() {
	}

	@Override
	public void execute(Tuple arg0) {
		String from = arg0.getString(0);
		String to = arg0.getString(1);
		Integer duration = arg0.getInteger(2);
		collector.emit(new Values(from + " - " + to, duration));
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("call", "duration"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
