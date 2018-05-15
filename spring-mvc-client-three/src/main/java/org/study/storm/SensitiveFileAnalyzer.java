package org.study.storm;

import java.util.Map;

import org.apache.storm.shade.com.google.common.base.Splitter;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * @Title: SensitiveFileAnalyzer
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 下午2:19:13
 * @see {@linkplain https://www.cnblogs.com/jietang/p/5423438.html}
 */
public class SensitiveFileAnalyzer extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String line = input.getString(0);
		Map<String, String> join = Splitter.on("&").withKeyValueSeparator("=").split(line);
		collector.emit(new Values((String) join.get(RubbishUsers.HOMECITY_COLUMNNAME),
				(String) join.get(RubbishUsers.USERID_COLUMNNAME), (String) join.get(RubbishUsers.MSISDN_COLUMNNAME),
				(String) join.get(RubbishUsers.SMSCONTENT_COLUMNNAME)));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(RubbishUsers.HOMECITY_COLUMNNAME, RubbishUsers.USERID_COLUMNNAME,
				RubbishUsers.MSISDN_COLUMNNAME, RubbishUsers.SMSCONTENT_COLUMNNAME));
	}

}
