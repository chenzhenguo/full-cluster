package org.study.hbase;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Title: TestCoprocessor
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月11日 下午1:14:38
 * @see {@linkplain http://blog.csdn.net/a2615381/article/details/51353659}
 */
public class TestCoprocessor extends BaseRegionObserver {

	/**
	 * 继承BaseRegionObserver类，实现prePut方法，在插入订单详情表之前，向索引表插入索引数据。
	 */
	@Override
	public void prePut(final ObserverContext<RegionCoprocessorEnvironment> context, final Put put, final WALEdit edit,
			final Durability writeToWAL) throws IOException {
		super.prePut(context, put, edit, writeToWAL);
		
		Configuration conf = new Configuration();
		HTable table = new HTable(conf, "index_table");
		List<Cell> kv = put.get("data".getBytes(), "name".getBytes());
		for (Cell tmp : kv) {
			Put indexPut = new Put(CellUtil.cloneValue(tmp));
			indexPut.addColumn("data".getBytes(), CellUtil.cloneRow(tmp), Bytes.toBytes(System.currentTimeMillis()));
			table.put(indexPut);
		}

		table.close();
	}

}
