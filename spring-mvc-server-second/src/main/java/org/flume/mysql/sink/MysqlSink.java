package org.flume.mysql.sink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

/**
 * 
 * @Title: MysqlSink.java
 * @Description: Flume 抓取日志文件存入MySQL中
 * @author zhaotf
 * @date 2018年1月28日 上午11:33:50
 * @see {@linkplain http://blog.csdn.net/poisions/article/details/51695372}
 */
public class MysqlSink extends AbstractSink implements Configurable {
	private Logger LOG = LoggerFactory.getLogger(MysqlSink.class);
	private String hostname;
	private String port;
	private String databaseName;
	// private String tableName;
	private String userName;
	private String password;
	private PreparedStatement preparedStatement;
	private Connection conn;
	private int batchSize;

	public MysqlSink() {
		LOG.info("MysqlSink start...");
	}

	@Override
	public synchronized void start() {
		super.start();
		// 调用Class.forName()方法加载驱动程序
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://" + hostname + ":" + port + "/" + databaseName;
			// 调用DriverManager对象的getConnection()方法，获得一个Connection对象
			conn = DriverManager.getConnection(url, userName, password);
			conn.setAutoCommit(false);
			// 创建一个Statement对象
			String sql = "INSERT INTO `" + databaseName
					+ "`.`t_s_log` (`id`,`broswer`, `logcontent`, `loglevel`, `note`, `operatetime`, `operatetype`, `userid`)"
					+ "VALUES (replace(uuid(), '-', ''),'x', ?, '1', '0.0.0.0', now(), '1', 'zhaotf')";
			preparedStatement = conn.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	@Override
	public synchronized void stop() {
		super.stop();
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void configure(Context context) {
		hostname = context.getString("hostname");
		Preconditions.checkNotNull(hostname, "hostname must be set!!");
		port = context.getString("port");
		Preconditions.checkNotNull(port, "port must be set!!");
		databaseName = context.getString("databaseName");
		Preconditions.checkNotNull(databaseName, "databaseName must be set!!");
		// tableName = context.getString("tableName");
		// Preconditions.checkNotNull(tableName, "tableName must be set!!");
		userName = context.getString("userName");
		Preconditions.checkNotNull(userName, "userName must be set!!");
		password = context.getString("password");
		Preconditions.checkNotNull(password, "password must be set!!");
		batchSize = context.getInteger("batchSize", 100);
		Preconditions.checkNotNull(batchSize > 0, "batchSize must be set!!");
	}

	@Override
	public Status process() throws EventDeliveryException {
		Status result = Status.READY;
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();
		Event event;
		String content;

		List<String> actions = Lists.newArrayList();
		transaction.begin();
		try {
			for (int i = 0; i < batchSize; i++) {
				event = channel.take();
				if (event != null) {
					content = new String(event.getBody());
					actions.add(content);
				} else {
					result = Status.BACKOFF;
					break;
				}
			}

			if (actions.size() > 0) {
				preparedStatement.clearBatch();
				for (String temp : actions) {
					preparedStatement.setString(1, temp);
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();

				conn.commit();
			}
			transaction.commit();
		} catch (Throwable e) {
			try {
				transaction.rollback();
			} catch (Exception e2) {
				LOG.error("Exception in rollback. Rollback might not have been" + "successful.", e2);
			}
			LOG.error("Failed to commit transaction." + "Transaction rolled back.", e);
			Throwables.propagate(e);
		} finally {
			transaction.close();
		}

		return result;
	}

}
