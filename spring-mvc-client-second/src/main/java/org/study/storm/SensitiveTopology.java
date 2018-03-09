package org.study.storm;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.storm.Config;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * @Title: SensitiveTopology
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 下午3:05:46
 * @see {@linkplain https://www.cnblogs.com/jietang/p/5423438.html}
 */
public class SensitiveTopology {
	// Spout/Bolt的ID定义
	public static final String SensitiveSpoutFuZhou = "SensitiveSpout591";
	public static final String SensitiveSpoutXiaMen = "SensitiveSpout592";
	public static final String SensitiveBoltAnalysis = "SensitiveBoltAnalysis";
	public static final String SensitiveBoltPersistence = "SensitiveBolPersistence";

	public static void main(String[] args) throws SQLException {
		System.out.println(StringUtils.center("SensitiveTopology", 40, "*"));

		TopologyBuilder builder = new TopologyBuilder();

		// 构建spout，分别设置并行度为2
		builder.setSpout(SensitiveSpoutFuZhou, new SensitiveFileReader(SensitiveFileReader.InputFuZhouPath), 2);
		builder.setSpout(SensitiveSpoutXiaMen, new SensitiveFileReader(SensitiveFileReader.InputXiaMenPath), 2);

		// 构建bolt设置并行度为4
		builder.setBolt(SensitiveBoltAnalysis, new SensitiveFileAnalyzer(), 4).shuffleGrouping(SensitiveSpoutFuZhou)
				.shuffleGrouping(SensitiveSpoutXiaMen);

		// 构建bolt设置并行度为4
		SensitiveBatchBolt persistenceBolt = new SensitiveBatchBolt();

		builder.setBolt(SensitiveBoltPersistence, persistenceBolt, 4).fieldsGrouping(SensitiveBoltAnalysis, new Fields(
				RubbishUsers.HOMECITY_COLUMNNAME, RubbishUsers.USERID_COLUMNNAME, RubbishUsers.MSISDN_COLUMNNAME));

		Config conf = new Config();
		conf.setDebug(true);
		// 设置worker，集群里面最大就8个slots了，全部使用上
		conf.setNumWorkers(8);
		// 3秒监控一次敏感信息入库MySQL情况
		conf.put("RUBBISHMONITOR_INTERVAL", 3);

		// 执行分布式拓扑
		try {
			StormRunner.runTopologyRemotely(builder.createTopology(), "SensitiveTopology", conf);
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}

	}

}
