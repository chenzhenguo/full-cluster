package com.hhcf.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Title: HbaseUtil.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年3月4日 下午3:30:24
 * @see {@linkplain http://blog.csdn.net/hjxgood/article/details/46912793}
 */
public class HbaseUtil {

	private static Connection conn;

	static {
		Properties systemProperties = System.getProperties();
		// systemProperties.setProperty("hadoop.home.dir",
		// "E:/project/isearch/hadoop");
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "computer-201,computer-202,computer-193");
		// conf.set("hbase.zookeeper.quorum",
		// "1.1.1.1,1.1.1.2,1.1.1.3");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		try {
			conn = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 建表
	 * 
	 * @param tableName
	 * @param columnFamily
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static void createTable(byte[] tableName, byte[]... columnFamilies)
			throws IllegalArgumentException, IOException {
		Admin admin = null;
		TableName table = TableName.valueOf(tableName);
		try {
			admin = conn.getAdmin();
			if (!admin.tableExists(table)) {
				HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(tableName));
				for (byte[] columnFamily : columnFamilies) {
					descriptor.addFamily(new HColumnDescriptor(columnFamily));
				}
				admin.createTable(descriptor);
			}
		} finally {
			// IOUtils.closeQuietly(admin);
			if (admin != null) {
				admin.close();
			}
		}

	}

	public static void dropTable(byte[] tableName) throws IOException {
		Admin admin = null;
		TableName table = TableName.valueOf(tableName);
		try {
			admin = conn.getAdmin();
			if (admin.tableExists(table)) {
				admin.disableTable(table);
				admin.deleteTable(table);
			}
		} finally {
			// IOUtils.closeQuietly(admin);
			if (admin != null) {
				admin.close();
			}
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param tableName
	 *            表名
	 * @param columnFamily
	 *            列族名
	 * @param rowKey
	 *            对应数据库中的主建
	 * @param columns
	 *            列信息
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static void add(byte[] tableName, byte[] columnFamily, byte[] row, Map<String, String> columns)
			throws IllegalArgumentException, IOException {
		Table table = null;
		try {
			table = conn.getTable(TableName.valueOf(tableName));
			Put put = new Put(row);
			for (Map.Entry<String, String> entry : columns.entrySet()) {
				put.addColumn(columnFamily, Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
			}
			table.put(put);
		} finally {
			closeTable(table);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param columnFamily
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static Map<byte[], byte[]> get(byte[] tableName, byte[] columnFamily, byte[] row)
			throws IllegalArgumentException, IOException {
		Table table = null;
		Map<byte[], byte[]> result = null;
		try {
			table = conn.getTable(TableName.valueOf(tableName));
			Get get = new Get(row);
			get.addFamily(columnFamily);
			Result r = table.get(get);
			result = r.getFamilyMap(columnFamily);
		} finally {
			closeTable(table);
		}
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param tableName
	 * @param row
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public static void delete(byte[] tableName, byte[] row) throws IllegalArgumentException, IOException {
		Table table = null;
		try {
			table = conn.getTable(TableName.valueOf(tableName));
			Delete del = new Delete(row);
			table.delete(del);
		} finally {
			closeTable(table);
		}

	}

	public static void closeTable(Table table) {
		if (table != null) {
			try {
				table.close();
			} catch (IOException ignore) {

			}
		}
	}

	public static Map<byte[], byte[]> get(String tableName, String columnFamily, String row)
			throws IllegalArgumentException, IOException {
		byte[] table_name = Bytes.toBytes(tableName);
		byte[] column_namily = Bytes.toBytes(columnFamily);
		byte[] row_ = Bytes.toBytes(row);
		Table table = null;
		Map<byte[], byte[]> result = null;
		try {
			table = conn.getTable(TableName.valueOf(table_name));
			Get get = new Get(row_);
			get.addFamily(column_namily);
			Result r = table.get(get);
			result = r.getFamilyMap(column_namily);
		} finally {
			closeTable(table);
		}
		return result;
	}

	public static <T> T getObject(Class<T> classObj, String tableName, String columnFamily, String row) {
		T result = null;
		byte[] table_name = Bytes.toBytes(tableName);
		byte[] column_namily = Bytes.toBytes(columnFamily);
		byte[] row_ = Bytes.toBytes(row);

		return result;
	}

	public static void close() throws IOException {
		// IOUtils.closeQuietly(conn);
		if (conn != null) {
			conn.close();
		}
	}

	public static void main(String[] args) throws IOException {
		byte[] tableName = Bytes.toBytes("wx_public_number");
		byte[] columnFamily = Bytes.toBytes("wx");
		byte[] row = Bytes.toBytes("0d0f649a673e7103a311586e5f5a7270");

		// dropTable(tableName);
		// createTable(tableName, columnFamily);
		// Map<String, String> columns = new HashMap<String, String>();
		// columns.put("title", "测试标题");
		// columns.put("content", "测试内容");
		// add(tableName, columnFamily, Bytes.toBytes(1), columns);

		Map<byte[], byte[]> columnValue = get(tableName, columnFamily, row);
		for (Map.Entry<byte[], byte[]> entry : columnValue.entrySet()) {
			System.out.println(Bytes.toString(entry.getKey()) + ":" + Bytes.toString(entry.getValue()));
		}
		close();
	}
}
