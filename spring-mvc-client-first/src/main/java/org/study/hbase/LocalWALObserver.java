package org.study.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.WALCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.WALObserver;
import org.apache.hadoop.hbase.regionserver.wal.HLogKey;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.wal.WALKey;

/**
 * 
 * @Title: LocalWALObserver
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月12日 下午3:06:09
 */
public class LocalWALObserver implements WALObserver {

	@Override
	public void start(CoprocessorEnvironment env) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(CoprocessorEnvironment env) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preWALWrite(ObserverContext<? extends WALCoprocessorEnvironment> ctx, HRegionInfo info,
			WALKey logKey, WALEdit logEdit) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean preWALWrite(ObserverContext<WALCoprocessorEnvironment> ctx, HRegionInfo info, HLogKey logKey,
			WALEdit logEdit) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void postWALWrite(ObserverContext<? extends WALCoprocessorEnvironment> ctx, HRegionInfo info, WALKey logKey,
			WALEdit logEdit) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postWALWrite(ObserverContext<WALCoprocessorEnvironment> ctx, HRegionInfo info, HLogKey logKey,
			WALEdit logEdit) throws IOException {
		// TODO Auto-generated method stub

	}

}
