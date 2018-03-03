package org.study.hbase.coprocessor;

import java.io.IOException;

import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Title: LocalApp.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月3日 下午3:56:31
 * @see {@linkplain http://blog.csdn.net/vegetable_bird_001/article/details/70070366}
 */
public class LocalApp extends BaseRegionObserver {
	private HTablePool pool = null;
	private final static String SOURCE_TABLE = "index_table";// 二级索引表

	@Override
	public void start(CoprocessorEnvironment env) throws IOException {
		pool = new HTablePool(env.getConfiguration(), 10);
	}

	@Override
	public void stop(CoprocessorEnvironment e) throws IOException {
		pool.close();
	}

	@Override
	public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability)
			throws IOException {
		try {
			String rowkey = Bytes.toString(put.getRow());
			// put.get(arg0, arg1)
			HTableInterface table = pool.getTable(Bytes.toBytes(SOURCE_TABLE));
			String pre = rowkey.substring(0, 2);
			if (pre.equals("aa") || pre.equals("ab") || pre.equals("ac") || pre.equals("ba") || pre.equals("bb")
					|| pre.equals("bc") || pre.equals("ca") || pre.equals("cb") || pre.equals("cc")) {
				String[] splits = rowkey.split("_");
				String uid = splits[0].substring(2);
				String timestamp = splits[1];
				String mid = "";
				for (int i = 2; i < splits.length; i++) {
					mid += splits[i];
					mid += "_";
				}
				mid = mid.substring(0, mid.length() - 1);
				String newRowkey = "t" + pre + timestamp + "_" + uid + "_" + mid;
				System.out.println(newRowkey);
				Put indexput2 = new Put(newRowkey.getBytes());
				indexput2.addColumn("cf1".getBytes(), "col-rowkey".getBytes(), rowkey.getBytes());// 放置索引
				table.put(indexput2);
			}
			table.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void prePut(final ObserverContext<RegionCoprocessorEnvironment> e, final Put put, final WALEdit edit,
			final Durability durability) throws IOException {
		try {
			String rowkey = Bytes.toString(put.getRow());
			HTableInterface table = pool.getTable(Bytes.toBytes(SOURCE_TABLE));
			String pre = rowkey.substring(0, 2);
			if (pre.equals("aa") || pre.equals("ab") || pre.equals("ac") || pre.equals("ba") || pre.equals("bb")
					|| pre.equals("bc") || pre.equals("ca") || pre.equals("cb") || pre.equals("cc")) {
				String[] splits = rowkey.split("_");
				String uid = splits[0].substring(2);
				String timestamp = splits[1];
				String mid = "";
				for (int i = 2; i < splits.length; i++) {
					mid += splits[i];
					mid += "_";
				}
				mid = mid.substring(0, mid.length() - 1);
				String newRowkey = "h" + pre + timestamp + "_" + uid + "_" + mid;
				System.out.println(newRowkey);
				Put indexput2 = new Put(newRowkey.getBytes());
				indexput2.addColumn("cf1".getBytes(), "col-rowkey".getBytes(), rowkey.getBytes());// 放置索引
				table.put(indexput2);
			}
			table.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
