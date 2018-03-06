package org.study.bjsxt.spark.dao;

import java.util.List;

import org.study.bjsxt.spark.domain.RandomExtractCar;
import org.study.bjsxt.spark.domain.RandomExtractMonitorDetail;

/**
 * 随机抽取car信息管理DAO类
 * @author root
 *
 */
public interface IRandomExtractDAO {
	void insertBatchRandomExtractCar(List<RandomExtractCar> carRandomExtracts);
	
	void insertBatchRandomExtractDetails(List<RandomExtractMonitorDetail> r);
	
}
