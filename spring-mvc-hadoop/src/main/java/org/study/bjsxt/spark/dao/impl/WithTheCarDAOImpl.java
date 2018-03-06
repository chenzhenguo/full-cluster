package org.study.bjsxt.spark.dao.impl;

import org.study.bjsxt.spark.constant.Constants;
import org.study.bjsxt.spark.dao.IWithTheCarDAO;
import org.study.bjsxt.spark.jdbc.JDBCHelper;
import org.study.bjsxt.spark.util.DateUtils;

public class WithTheCarDAOImpl implements IWithTheCarDAO {

	@Override
	public void updateTestData(String cars) {
		JDBCHelper jdbcHelper = JDBCHelper.getInstance();
		String sql = "UPDATE task set task_param = ? WHERE task_id = 3";
		Object[] params = new Object[]{"{\"startDate\":[\""+DateUtils.getTodayDate()+"\"],\"endDate\":[\""+DateUtils.getTodayDate()+"\"],\""+Constants.FIELD_CARS+"\":[\""+cars+"\"]}"};
		jdbcHelper.executeUpdate(sql, params);
	}

}
