package org.study.storm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.storm.shade.org.apache.commons.collections.Predicate;
import org.apache.storm.shade.org.apache.commons.collections.iterators.FilterIterator;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IBasicBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Title: SensitiveBatchBolt
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 下午2:23:20
 * @see {@linkplain https://www.cnblogs.com/jietang/p/5423438.html}
 */
public class SensitiveBatchBolt implements IBasicBolt {
	private static final long serialVersionUID = 1L;
	// Hibernate配置加载
	private final static String HIBERNATE_APPLICATIONCONTEXT = "newlandframework/storm/resource/jdbc-hibernate-bean.xml";
	// Spring、Hibernate上下文不要序列化
	private static transient ApplicationContext hibernate = new ClassPathXmlApplicationContext(
			HIBERNATE_APPLICATIONCONTEXT);
	private static transient SessionFactory sessionFactory = (SessionFactory) hibernate.getBean("sessionFactory");
	private static List<String> list = new ArrayList<String>(Arrays.asList(RubbishUsers.SENSITIVE_KEYWORDS));

	public SensitiveBatchBolt() throws SQLException {
		super();
	}

	/**
	 * 敏感信息数据源,可以考虑放入缓存或者数据库中加载判断
	 */
	private class SensitivePredicate implements Predicate {
		private String sensitiveWord = null;

		SensitivePredicate(String sensitiveWord) {
			this.sensitiveWord = sensitiveWord;
		}

		public boolean evaluate(Object object) {
			return this.sensitiveWord.contains((String) object);
		}
	}

	// Monitor线程定期打印监控采集处理情况
	class SensitiveMonitorThread implements Runnable {
		private int sensitiveMonitorTimeInterval = 0;
		private Session session = null;

		SensitiveMonitorThread(int sensitiveMonitorTimeInterval) {
			this.sensitiveMonitorTimeInterval = sensitiveMonitorTimeInterval;
			session = sessionFactory.openSession();
		}

		public void run() {
			while (true) {
				try {
					Criteria criteria1 = session.createCriteria(RubbishUsers.class);

					criteria1.add(
							Restrictions.and(
									Restrictions.or(Restrictions.like("smsContent",
											StringUtils.center(RubbishUsers.SENSITIVE_KEYWORD1,
													RubbishUsers.SENSITIVE_KEYWORD1.length() + 2, "%"),
											MatchMode.ANYWHERE),
											Restrictions.like("smsContent",
													StringUtils.center(RubbishUsers.SENSITIVE_KEYWORD2,
															RubbishUsers.SENSITIVE_KEYWORD2.length() + 2, "%"),
													MatchMode.ANYWHERE)),
									Restrictions.in("homeCity", RubbishUsers.SENSITIVE_HOMECITYS)));

					List<RubbishUsers> rubbishList = (List<RubbishUsers>) criteria1.list();

					System.out.println(StringUtils.center("[SensitiveTrace 敏感用户清单如下]", 40, "-"));

					if (rubbishList != null) {
						System.out.println("[SensitiveTrace 敏感用户数量]:" + rubbishList.size());
						for (RubbishUsers rubbish : rubbishList) {
							System.out.println(rubbish + rubbish.getSmsContent());
						}
					} else {
						System.out.println("[SensitiveTrace 敏感用户数量]:0");
					}
				} catch (HibernateException e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(sensitiveMonitorTimeInterval * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 分布式环境下面的要同步控制
	 * 
	 * @param input
	 */
	private synchronized void save(Tuple input) {
		Session session = sessionFactory.openSession();

		try {
			RubbishUsers users = new RubbishUsers();
			users.setUserId(Integer.parseInt(input.getStringByField(RubbishUsers.USERID_COLUMNNAME)));
			users.setHomeCity(Integer.parseInt(input.getStringByField(RubbishUsers.HOMECITY_COLUMNNAME)));
			users.setMsisdn(Integer.parseInt(input.getStringByField(RubbishUsers.MSISDN_COLUMNNAME)));
			users.setSmsContent(input.getStringByField(RubbishUsers.SMSCONTENT_COLUMNNAME));

			Predicate isSensitiveFileAnalysis = new SensitivePredicate(
					(String) input.getStringByField(RubbishUsers.SMSCONTENT_COLUMNNAME));

			FilterIterator iterator = new FilterIterator(list.iterator(), isSensitiveFileAnalysis);

			if (iterator.hasNext()) {
				session.beginTransaction();
				// 入库MySQL
				session.save(users);
				session.getTransaction().commit();
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	/**
	 * 很多情况下面storm运行期执行报错，都是由于execute有异常导致的，重点观察execute的函数逻辑 最经常报错的情况是报告：ERROR
	 * backtype.storm.daemon.executor -
	 * java.lang.RuntimeException:java.lang.NullPointerException
	 * backtype.storm.utils.DisruptorQueue.consumeBatchToCursor(DisruptorQueue.
	 * java ...) 类似这样的错误，有点莫名其妙，开始都运行的很正常，后面忽然就报空指针异常了，我开始以为是storm部署的问题，
	 * 后面jstack跟踪发现，主要还是execute逻辑的问题，所以遇到这类的问题不要手忙脚乱，适当结合jstack跟踪定位
	 */
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		save(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		final int sensitiveMonitorTimeInterval = Integer.parseInt(stormConf.get("RUBBISHMONITOR_INTERVAL").toString());
		SensitiveMonitorThread montor = new SensitiveMonitorThread(sensitiveMonitorTimeInterval);
		new Thread(montor).start();
	}

	@Override
	public void cleanup() {
	}

}
