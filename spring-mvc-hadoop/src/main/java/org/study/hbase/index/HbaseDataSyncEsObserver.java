package org.study.hbase.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @Title: HbaseDataSyncEsObserver.java
 * @Description: TODO 使用Hbase协作器(Coprocessor)同步数据到ElasticSearch
 * @see 然后编写一个Class类，继承BaseRegionObserver，并复写其中的start()、stop()、postPut()、
 *      postDelete()、四个方法。这四个方法其实很好理解，分别表示协作器开始、协作器结束、
 *      put事件触发并将数据存入hbase之后我们可以做一些事情，delete事件触发并将数据从hbase删除之后我们可以做一些事情。
 *      我们只要将初始化ES客户端的代码写在start中，在stop中关闭ES客户端以及定义好的Scheduled对象即可。两个触发事件分别bulk
 *      hbase中的数据到ES，就轻轻松松的搞定了。
 * @author zhaotf
 * @date 2018年3月4日 下午1:37:44
 * @see {@linkplain http://blog.csdn.net/fxsdbt520/article/details/53884338}
 */
public class HbaseDataSyncEsObserver extends BaseRegionObserver {
	private static final Log LOG = LogFactory.getLog(HbaseDataSyncEsObserver.class);

	/**
	 * read es config from params
	 * 
	 * @param env
	 */
	private static void readConfiguration(CoprocessorEnvironment env) {
		Configuration conf = env.getConfiguration();
		ESClient.clusterName = conf.get("es_cluster");
		ESClient.nodeHost = conf.get("es_host");
		ESClient.nodePort = conf.getInt("es_port", -1);
		ESClient.indexName = conf.get("es_index");
		ESClient.typeName = conf.get("es_type");
	}

	/**
	 * start
	 * 
	 * @param e
	 * @throws IOException
	 */
	@Override
	public void start(CoprocessorEnvironment e) throws IOException {
		// read config
		readConfiguration(e);
		// init ES client
		ESClient.initEsClient();
		LOG.error("------observer init EsClient ------" + ESClient.getInfo());
	}

	/**
	 * stop
	 * 
	 * @param e
	 * @throws IOException
	 */
	@Override
	public void stop(CoprocessorEnvironment e) throws IOException {
		// close es client
		ESClient.closeEsClient();
		// shutdown time task
		ElasticSearchBulkOperator.shutdownScheduEx();
	}

	/**
	 * Called after the client stores a value after data put to hbase then
	 * prepare update builder to bulk ES
	 *
	 * @param e
	 * @param put
	 * @param edit
	 * @param durability
	 * @throws IOException
	 */
	@Override
	public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability)
			throws IOException {
		String indexId = new String(put.getRow());
		try {
			NavigableMap<byte[], List<Cell>> familyMap = put.getFamilyCellMap();
			Map<String, Object> infoJson = new HashMap<String, Object>();
			Map<String, Object> json = new HashMap<String, Object>();
			for (Map.Entry<byte[], List<Cell>> entry : familyMap.entrySet()) {
				for (Cell cell : entry.getValue()) {
					String key = Bytes.toString(CellUtil.cloneQualifier(cell));
					String value = Bytes.toString(CellUtil.cloneValue(cell));
					json.put(key, value);
				}
			}
			// set hbase family to es
			infoJson.put("info", json);
			ElasticSearchBulkOperator.addUpdateBuilderToBulk(
					ESClient.client.prepareUpdate(ESClient.indexName, ESClient.typeName, indexId).setDocAsUpsert(true)
							.setDoc(infoJson));
		} catch (Exception ex) {
			LOG.error("observer put  a doc, index [ " + ESClient.indexName + " ]" + "indexId [" + indexId + "] error : "
					+ ex.getMessage());
		}
	}

	/**
	 * Called after the client deletes a value. after data delete from hbase
	 * then prepare delete builder to bulk ES
	 * 
	 * @param e
	 * @param delete
	 * @param edit
	 * @param durability
	 * @throws IOException
	 */
	@Override
	public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, Delete delete, WALEdit edit,
			Durability durability) throws IOException {
		String indexId = new String(delete.getRow());
		try {
			ElasticSearchBulkOperator.addDeleteBuilderToBulk(
					ESClient.client.prepareDelete(ESClient.indexName, ESClient.typeName, indexId));
		} catch (Exception ex) {
			LOG.error(ex);
			LOG.error("observer delete  a doc, index [ " + ESClient.indexName + " ]" + "indexId [" + indexId
					+ "] error : " + ex.getMessage());

		}
	}
}
