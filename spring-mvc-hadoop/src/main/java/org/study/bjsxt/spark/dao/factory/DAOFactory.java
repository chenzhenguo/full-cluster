package org.study.bjsxt.spark.dao.factory;

import org.study.bjsxt.spark.dao.IAreaDao;
import org.study.bjsxt.spark.dao.ICarTrackDAO;
import org.study.bjsxt.spark.dao.IMonitorDAO;
import org.study.bjsxt.spark.dao.IRandomExtractDAO;
import org.study.bjsxt.spark.dao.ITaskDAO;
import org.study.bjsxt.spark.dao.IWithTheCarDAO;
import org.study.bjsxt.spark.dao.impl.AreaDaoImpl;
import org.study.bjsxt.spark.dao.impl.CarTrackDAOImpl;
import org.study.bjsxt.spark.dao.impl.MonitorDAOImpl;
import org.study.bjsxt.spark.dao.impl.RandomExtractDAOImpl;
import org.study.bjsxt.spark.dao.impl.TaskDAOImpl;
import org.study.bjsxt.spark.dao.impl.WithTheCarDAOImpl;

/**
 * DAO工厂类
 * @author root
 *
 */
public class DAOFactory {
	
	
	public static ITaskDAO getTaskDAO(){
		return new TaskDAOImpl();
	}
	
	public static IMonitorDAO getMonitorDAO(){
		return new MonitorDAOImpl();
	}
	
	public static IRandomExtractDAO getRandomExtractDAO(){
		return new RandomExtractDAOImpl();
	}
	
	public static ICarTrackDAO getCarTrackDAO(){
		return new CarTrackDAOImpl();
	}
	
	public static IWithTheCarDAO getWithTheCarDAO(){
		return new WithTheCarDAOImpl();
	}

	public static IAreaDao getAreaDao() {
		return  new AreaDaoImpl();
		
	}
}
