package org.study.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.CoprocessorService;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;

import com.google.protobuf.Service;

/**
 * @Title: TestEndpoint
 * @Description:
 * @Author: zhaotf
 * @Since:2018年2月11日 下午2:03:12
 * @see {@linkplain https://www.cnblogs.com/ios123/p/6379407.html}
 */
public class TestEndpoint implements Coprocessor, CoprocessorService {
	// 单个region的上下文环境信息
	private RegionCoprocessorEnvironment env;

	@Override
	public Service getService() {
		return null;
	}

	@Override
	public void start(CoprocessorEnvironment env) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(CoprocessorEnvironment env) throws IOException {
		// TODO Auto-generated method stub

	}

}
