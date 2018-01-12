package com.hhcf.learn.other;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.hhcf.learn.model.Employee;

/**
 * 
 * @Title: BatisEmployeeDao
 * @Description:
 * @Author: zhaotf
 * @Since:2017年7月12日 上午11:34:21
 * @Version:1.0
 */
public class BatisEmployeeDao {
	public List<Employee> getAllEmployees() {
		SqlSession session = MySqlSession.newSqlSession();
		try {
			List<Employee> ret = session.selectList("selectAllEmployee");
			return ret;
		} finally {
			session.close();
		}
	}

	public Employee getEmployeeById(Long id) {
		SqlSession session = MySqlSession.newSqlSession();
		try {
			Employee employee = session.selectOne("selectEmployeeById", id);
			return employee;
		} finally {
			session.close();
		}
	}
}
