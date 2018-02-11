package org.study.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Title: HbaseCoprocessor
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月11日 上午10:57:11
 * @see {@linkplain http://blog.csdn.net/a2615381/article/details/52174671}
 */
public class HbaseCoprocessor {

	public static void main(String[] args) throws Throwable {
		Configuration conf = HBaseConfiguration.create();
		System.setProperty("hadoop.home.dir", "E:/hadoop");
		myHbaseConf(conf);
		rowCount2("a02", conf);// * 通过协处理器获得行数,不修改 hbase.site
	}

	private static void rowCount2(String tableName, Configuration conf) throws IllegalArgumentException, Throwable {
		String coprocessorClassName = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor htd = admin.getTableDescriptor(Bytes.toBytes(tableName));
		boolean flag = htd.hasCoprocessor(coprocessorClassName);// 有就是true,没有就是false
		if (!flag) {
			admin.disableTable(tableName);
			htd.addCoprocessor(coprocessorClassName);
			admin.modifyTable(Bytes.toBytes(tableName), htd);
			admin.enableTable(tableName);
		}

		Scan scan = new Scan();
		scan.addFamily(Bytes.toBytes("info"));
		long rowCount = 0;
		AggregationClient ac = new AggregationClient(conf);
		rowCount = ac.rowCount(TableName.valueOf(Bytes.toBytes(tableName)), new LongColumnInterpreter(), scan);
		System.out.println(rowCount);
	}

	/**
	 * 设置参数
	 */
	private static void myHbaseConf(Configuration conf) {
		conf.set("hbase.zookeeper.quorum", "hserver131:2181,hserver132:2181,hserver133:2181");
	}

}
