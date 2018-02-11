package org.study.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.Region;
import org.apache.hadoop.hbase.regionserver.RegionServerServices;

/**
 * @Title: RowCountObserver
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月11日 下午1:58:42
 * @see {@linkplain http://blog.csdn.net/yangzhiyouvl/article/details/52280847}
 */
public class RowCountObserver extends BaseRegionObserver {
	RegionCoprocessorEnvironment env;

	@Override
	public void start(CoprocessorEnvironment e) throws IOException {
		env = (RegionCoprocessorEnvironment) e;
		RegionServerServices rss = env.getRegionServerServices();
		Region m_region = env.getRegion();
		zNodePath = zNodePath + m_region.getRegionNameAsString();
		zkw = rss.getZooKeeper();
		myrowcount = 0; // count;
		try {
			if (ZKUtil.checkExists(zkw, zNodePath) == -1) {
				LOG.error("LIULIUMI: cannot find the znode");
				ZKUtil.createWithParents(zkw, zNodePath);
				LOG.info("znode path is : " + zNodePath);
			}
		} catch (Exception ee) {
			LOG.error("LIULIUMI: create znode fail");
		}
	}

	@Override
	public void stop(CoprocessorEnvironment e) throws IOException {
		// nothing to do here
	}
}
