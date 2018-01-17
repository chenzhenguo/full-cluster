package com.hhcf.learn.other;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 
 * @Title: MySqlSession
 * @Description:myBatis原理学习
 * @Author: zhaotf
 * @Since:2017年7月12日 上午11:33:26
 * @Version:1.0
 */
public class MySqlSession {
	private static String mybatisResource = "mybatis-config.xml";

	private static SqlSessionFactory sqlSessionFactory;

	public static SqlSessionFactory getSqlSessionFactory() {
		try {
			if (sqlSessionFactory == null) {
				InputStream inputStream = Resources.getResourceAsStream(mybatisResource);
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			}

			return sqlSessionFactory;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static SqlSession newSqlSession() {
		SqlSession session = getSqlSessionFactory().openSession();
		return session;
	}
}
