package org.study.hbase.coprocessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.BaseRegionServerObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Title: App
 * @Description:hbase二级索引
 * @Author: zhaotf
 * @Since:2018年2月28日 上午7:38:37
 * @see {@linkplain http://blog.csdn.net/vegetable_bird_001/article/details/70070366}
 */
public class App extends BaseRegionObserver {
	private HTablePool pool = null;
	private final static String SOURCE_TABLE = "test";

	@Override
	public void start(CoprocessorEnvironment env) throws IOException {
		pool = new HTablePool(env.getConfiguration(), 10);
	}

	/**
	 * 将原有列rowkey，替换为自定义的
	 * 
	 * @param e
	 * @param get
	 * @param results
	 * @throws IOException
	 */
	@Override
	public void postGetOp(final ObserverContext<RegionCoprocessorEnvironment> e, final Get get,
			final List<Cell> results) throws IOException {
		HTableInterface table = pool.getTable(SOURCE_TABLE);
		String newRowKey = Bytes.toString(get.getRow());
		String pre = newRowKey.substring(0, 1);
		if (pre.equals("t")) {
			String[] splits = newRowKey.split("_");
			String prepre = splits[0].substring(1, 3);
			String timestamp = splits[0].substring(3);
			String uid = splits[1];
			String mid = StringUtils.EMPTY;
			for (int i = 2; i < splits.length; i++) {
				mid += splits[i];
				mid += "_";
			}
			mid = mid.substring(0, mid.length() - 1);
			String rowkey = prepre + uid + "_" + timestamp + "_" + mid;
			System.out.println("postGetOp-aaaaaaaaa:" + rowkey);
			Get realGet = new Get(rowkey.getBytes());
			Result result = table.get(realGet);

			List<Cell> cells = result.listCells();
			results.clear();// 清空原有列
			for (Cell cell : cells) {
				results.add(cell);
			}
		}
	}

	@Override
	public void postPut(final ObserverContext<RegionCoprocessorEnvironment> e, final Put put, final WALEdit edit,
			final Durability durability) throws IOException {
		String rowkey = Bytes.toString(put.getRow());
		HTableInterface table = pool.getTable(SOURCE_TABLE);
		String pre = rowkey.substring(0, 2);
		if (pre.equals("aa") || pre.equals("ab") || pre.equals("ac") || pre.equals("ba") || pre.equals("bb")
				|| pre.equals("bc") || pre.equals("ca") || pre.equals("cb") || pre.equals("cc")) {
			String[] splits = rowkey.split("_");
			String uid = splits[0].substring(2);
			String timestamp = splits[0];
			String mid = StringUtils.EMPTY;
			for (int i = 2; i < splits.length; i++) {
				mid += splits[i];
				mid += "_";
			}
			mid = mid.substring(0, mid.length() - 1);
			String newRowkey = "t" + pre + timestamp + "_" + uid + "_" + mid;
			System.out.println("postPut-aaaaaaaaaa:" + newRowkey);
			Put indexput2 = new Put(newRowkey.getBytes());
			indexput2.addColumn("relation".getBytes(), "column10".getBytes(), "456".getBytes());
			table.put(indexput2);
		}
		table.close();
	}

	@Override
	public boolean postScannerNext(final ObserverContext<RegionCoprocessorEnvironment> e, final InternalScanner s,
			final List<Result> results, final int limit, final boolean hasMore) throws IOException {
		HTableInterface table = pool.getTable(SOURCE_TABLE);
		List<Result> newresults = new ArrayList<Result>();
		for (Result rslt : results) {
			String newRowkey = Bytes.toString(rslt.getRow());
			String pre = newRowkey.substring(0, 1);
			if (pre.equals("t")) {
				String[] splits = newRowkey.split("_");
				String prepre = splits[0].substring(1, 3);
				String timestamp = splits[0].substring(3);
				String uid = splits[1];
				String mid = StringUtils.EMPTY;
				for (int i = 2; i < splits.length; i++) {
					mid += splits[i];
					mid += "_";
				}
				mid = mid.substring(0, mid.length() - 1);
				String rowkey = prepre + uid + "_" + timestamp + "_" + mid;
				Get realget = new Get(rowkey.getBytes());
				Result rewrslt = table.get(realget);
				newresults.add(rewrslt);
			}
		}
		results.clear();
		for (Result rlst : newresults) {
			results.add(rlst);
		}

		return hasMore;
	}

	@Override
	public void stop(CoprocessorEnvironment e) throws IOException {
		pool.close();
	}

	@Override
	public void preOpen(ObserverContext<RegionCoprocessorEnvironment> e) throws IOException {
		ConnectionFactory.createConnection().getTable(TableName.valueOf(SOURCE_TABLE)); // 最新实例化方法，待验证

	}

	@Override
	public void postOpen(ObserverContext<RegionCoprocessorEnvironment> e) {
	}

}
