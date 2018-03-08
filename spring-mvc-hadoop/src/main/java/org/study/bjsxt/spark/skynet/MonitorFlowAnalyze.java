package org.study.bjsxt.spark.skynet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.study.bjsxt.spark.conf.ConfigurationManager;
import org.study.bjsxt.spark.constant.Constants;
import org.study.bjsxt.spark.dao.IAreaDao;
import org.study.bjsxt.spark.dao.IMonitorDAO;
import org.study.bjsxt.spark.dao.ITaskDAO;
import org.study.bjsxt.spark.dao.factory.DAOFactory;
import org.study.bjsxt.spark.domain.Area;
import org.study.bjsxt.spark.domain.MonitorState;
import org.study.bjsxt.spark.domain.Task;
import org.study.bjsxt.spark.domain.TopNMonitor2CarCount;
import org.study.bjsxt.spark.domain.TopNMonitorDetailInfo;
import org.study.bjsxt.spark.util.DateUtils;
import org.study.bjsxt.spark.util.ParamUtils;
import org.study.bjsxt.spark.util.SparkUtils;
import org.study.bjsxt.spark.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import scala.Tuple2;

/**
 * 卡口流量监控模块 1、卡口数量的正常数量，异常数量，还有通道数同时查询出车流量排名前N的卡口 持久化到数据库中
 * 2、根据指定的卡口号和查询日期查询出此时的卡口的流量信息
 * 3、基于2功能点的基础上多维度搜车，通过车牌颜色，车辆类型，车辆颜色，车辆品牌，车辆型号，车辆年款信息进行多维度搜索，这个功能点里面会使用到异构数据源。
 * 
 * @see 日期-卡口编码-摄像头编码-车牌号-通过时间-？-？-？
 * @see 2017-04-25 0001 09203 京W47147 2017-04-25 20:58:17 138 49 04
 * @see 2017-04-25 0005 06975 京W47147 2017-04-25 20:12:39 50 10 06
 * 
 * @author root
 */
public class MonitorFlowAnalyze {
	public static void main(String[] args) {
		// 构建Spark运行时的环境参数
		SparkConf conf = new SparkConf().setAppName(Constants.SPARK_APP_NAME_SESSION)
		// .set("spark.sql.shuffle.partitions", "10")
		// .set("spark.default.parallelism", "100")
		// .set("spark.storage.memoryFraction", "0.5")
		// .set("spark.shuffle.consolidateFiles", "true")
		// .set("spark.shuffle.file.buffer", "64")
		// .set("spark.shuffle.memoryFraction", "0.3")
		// .set("spark.reducer.maxSizeInFlight", "24")
		// .set("spark.shuffle.io.maxRetries", "60")
		// .set("spark.shuffle.io.retryWait", "60")
		// .set("spark.serializer",
		// "org.apache.spark.serializer.KryoSerializer")
		// .registerKryoClasses(new Class[]{
		// SpeedSortKey.class})
		;

		/**
		 * 设置spark运行时的master 根据配置文件来决定的
		 */
		SparkUtils.setMaster(conf);

		JavaSparkContext sc = new JavaSparkContext(conf);
		/**
		 * 查看配置文件是否是本地测试，若是本地测试那么创建一个SQLContext 如果是集群测试HiveContext
		 */
		SQLContext sqlContext = SparkUtils.getSQLContext(sc);

		/**
		 * 基于本地测试生成模拟测试数据，如果在集群中运行的话，直接操作Hive中的表就可以 本地模拟数据注册成一张临时表
		 * monitor_flow_action monitor_camera_info
		 */
		SparkUtils.mockData(sc, sqlContext);

		/**
		 * 从配置文件中拿到spark.local.taskId.monitorFlow的taskId
		 */
		long taskId = ParamUtils.getTaskIdFromArgs(args, Constants.SPARK_LOCAL_TASKID_MONITOR);

		/**
		 * 获取ITaskDAO的对象，通过taskId查询出来的数据封装到Task（自定义）对象
		 */
		ITaskDAO taskDAO = DAOFactory.getTaskDAO();
		Task task = taskDAO.findTaskById(taskId);

		if (task == null) {
			return;
		}

		/**
		 * task.getTaskParams()是一个json格式的字符串 封装到taskParamsJsonObject
		 */
		JSONObject taskParamsJsonObject = JSONObject.parseObject(task.getTaskParams());

		/**
		 * taskParamsJsonObject这个对象中保存了 用户的条件 sqlContext：执行sql语句
		 * 通过params（json字符串）查询monitor_flow_action
		 */
		JavaRDD<Row> cameraRDD = SparkUtils.getCameraRDDByDateRange(sqlContext, taskParamsJsonObject);

		cameraRDD = cameraRDD.cache();

		Accumulator<String> monitorAndCameraStateAccumulator = sc.accumulator("",
				new MonitorAndCameraStateAccumulator());

		/**
		 * K:monitor_id V:row
		 */
		JavaPairRDD<String, Row> monitor2DetailRDD = getMonitor2DetailRDD(cameraRDD);

		monitor2DetailRDD = monitor2DetailRDD.cache();

		/**
		 * monitorId2RowsRDD K:monitor_id V:[row,row,row]
		 */
		JavaPairRDD<String, Iterable<Row>> monitorId2RowsRDD = monitor2DetailRDD.groupByKey();

		monitorId2RowsRDD = monitorId2RowsRDD.cache();

		/**
		 * aggregateMonitorId2DetailRDD K:monitor_id V:拼接的字符串
		 * Constants.FIELD_MONITOR_ID+"="+monitorId+"|"+Constants.FIELD_AREA_ID+
		 * "="+areaId+"|"+Constants.FIELD_CAMERA_IDS+"="+tmpInfos.toString().
		 * substring(1)+"|"+Constants.FIELD_CAMERA_COUNT+"="+cameraCount+"|"+
		 * Constants.FIELD_CAR_COUNT+"="+count;
		 */
		JavaPairRDD<String, String> aggregateMonitorId2DetailRDD = aggreagteByMonitor(monitorId2RowsRDD);

		/**
		 * carCount2MonitorRDD K:carCount V:monitor_id
		 */
		JavaPairRDD<Integer, String> carCount2MonitorRDD = checkMonitorState(sc, sqlContext,
				aggregateMonitorId2DetailRDD, taskId, taskParamsJsonObject, monitorAndCameraStateAccumulator);
		carCount2MonitorRDD.take(Integer.parseInt(args[0]));
		String value = monitorAndCameraStateAccumulator.value();
		System.out.println(value);
		/**
		 * 车流量最高的前5个卡扣号
		 */
		// List<String> topNMonitorIds = getTopNMonitorCarFlow(sc, taskId,
		// taskParamsJsonObject, carCount2MonitorRDD);

		/**
		 * * 保存累加器中的值 必须在action类算子后，再保存数据，不然的话，累加器中没有值
		 */
		// saveMonitorState(taskId, monitorAndCameraStateAccumulator);
		//
		// // 获取topN 卡口的具体信息
		// getTopNDetails(taskId, topNMonitorIds, monitorId2RowsRDD);
		//
		// List<String> top5MonitorIds = speedTopNMonitor(monitorId2RowsRDD);
		// for (String monitorId : top5MonitorIds) {
		// System.out.println("monitorId:" + monitorId);
		// }
		//
		// getMonitorDetails(sc, taskId, top5MonitorIds, monitorId2RowsRDD);
		//
		//// getCarTracker(sqlContext);
		//
		//
		// carPeng(sqlContext);

		sc.close();
	}

	private static void getCarTracker(SQLContext sqlContext) {
		HashMap<String, String> map = new HashMap<>();
		map.put("url", "jdbc:mysql://hadoop1:3306/traffic");
		map.put("user", "spark");
		map.put("driver", "com.mysql.jdbc.Driver");
		map.put("password", "spark2016");
		map.put("dbtable", "topn_monitor_detail_info");
		Dataset<Row> df = sqlContext.read().format("jdbc").options(map).load();

		JavaRDD<Row> rdd = df.javaRDD();
		rdd.mapToPair(new PairFunction<Row, String, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, String> call(Row row) throws Exception {
				String car = row.getAs("car") + "";
				String monitorId = row.getAs("monitor_id") + "";
				String actionTime = row.getAs("action_time") + "";
				return new Tuple2<String, String>(car, monitorId + "|" + actionTime);
			}
		}).groupByKey().foreach(new VoidFunction<Tuple2<String, Iterable<String>>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Iterable<String>> tuple) throws Exception {
				String car = tuple._1;
				Iterator<String> iterator = tuple._2.iterator();

				List<String> list = new ArrayList<>();

				while (iterator.hasNext()) {
					String str = iterator.next();
					list.add(str);
				}

				Collections.sort(list, new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						String actionTime1 = o1.split("\\|")[1];
						String actionTime2 = o2.split("\\|")[1];
						return DateUtils.after(actionTime1, actionTime2) ? 1 : -1;
					}
				});
				StringBuilder tracker = new StringBuilder();

				for (String str : list) {
					tracker.append("~" + str.split("\\|")[0] + "=" + str.split("\\|")[1]);
				}

				System.out.println(car + " tracker is:" + tracker.toString().substring(1));
			}
		});
	}

	@SuppressWarnings("resource")
	private static void carPeng(SQLContext sqlContext) {
		List<String> monitorIds1 = Arrays.asList("0001", "0002", "0003", "0004");
		List<String> monitorIds2 = Arrays.asList("0006", "0007", "0008", "0005");
		String startTime = "'2017-09-21 00:00:00'";
		String endTime = "'2017-09-21 23:59:59'";
		// 通过两堆卡扣号，分别取数据库（本地模拟的两张表）中查询数据
		JavaRDD<Row> areaRDD1 = getAreaRDDByMonitorIds(sqlContext, startTime, endTime, monitorIds1);
		JavaRDD<Row> areaRDD2 = getAreaRDDByMonitorIds(sqlContext, startTime, endTime, monitorIds2);

		JavaPairRDD<String, String> car2ActionTime1 = areaRDD1.mapToPair(new PairFunction<Row, String, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, String> call(Row row) throws Exception {
				String car = (String) row.getAs("car");
				return new Tuple2<String, String>(car, Constants.FIELD_MONITOR_ID + "=" + row.getAs("monitor_id") + "|"
						+ Constants.FIELD_ACTION_TIME + "=" + row.getAs("action_time"));
			}
		});

		JavaPairRDD<String, String> car2ActionTime2 = areaRDD2.mapToPair(new PairFunction<Row, String, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, String> call(Row row) throws Exception {
				String car = (String) row.getAs("car");
				return new Tuple2<String, String>(car, Constants.FIELD_MONITOR_ID + "=" + row.getAs("monitor_id") + "|"
						+ Constants.FIELD_ACTION_TIME + "=" + row.getAs("action_time"));
			}
		});

		JavaPairRDD<String, Tuple2<String, String>> joinRDD = car2ActionTime1.join(car2ActionTime2);

		JavaPairRDD<Long, String> minus2InfoRDD = joinRDD
				.mapToPair(new PairFunction<Tuple2<String, Tuple2<String, String>>, Long, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<Long, String> call(Tuple2<String, Tuple2<String, String>> tuple) throws Exception {
						String actionTime1 = StringUtils.getFieldFromConcatString(tuple._2._1, "\\|",
								Constants.FIELD_ACTION_TIME);
						String actionTime2 = StringUtils.getFieldFromConcatString(tuple._2._2, "\\|",
								Constants.FIELD_ACTION_TIME);
						Long key = Long.parseLong(DateUtils.minus(actionTime1, actionTime2) + "");
						String monitorId1 = StringUtils.getFieldFromConcatString(tuple._2._1, "\\|",
								Constants.FIELD_MONITOR_ID);
						String monitorId2 = StringUtils.getFieldFromConcatString(tuple._2._2, "\\|",
								Constants.FIELD_MONITOR_ID);
						String car = tuple._1;
						String value = Constants.FIELD_CAR + "=" + car + "|" + Constants.FIELD_MONITOR_ID + "="
								+ monitorId1 + "~" + monitorId2;
						return new Tuple2<Long, String>(key, value);
					}
				});

		List<Tuple2<String, String>> takeList = minus2InfoRDD.sortByKey()
				.mapToPair(new PairFunction<Tuple2<Long, String>, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<Long, String> tuple) throws Exception {
						Long minus = tuple._1;
						String monitorIds = StringUtils.getFieldFromConcatString(tuple._2, "\\|",
								Constants.FIELD_MONITOR_ID);
						String car = StringUtils.getFieldFromConcatString(tuple._2, "\\|", Constants.FIELD_CAR);
						return new Tuple2<String, String>(car, minus + "=" + monitorIds);
					}
				}).take(10);

		for (Tuple2<String, String> tuple : takeList) {
			System.out.println(tuple);
		}

	}

	private static JavaPairRDD<String, Object> getCar2DetailRDD(JavaRDD<Row> areaRowRDD1) {
		return areaRowRDD1.mapToPair(new PairFunction<Row, String, Object>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Object> call(Row row) throws Exception {
				return new Tuple2<String, Object>(row.getString(3), null);
			}
		});
	}

	private static JavaRDD<Row> getAreaRDDByMonitorIds(SQLContext sqlContext, String startTime, String endTime,
			List<String> monitorId1) {
		String sql = "SELECT * " + "FROM monitor_flow_action" + " WHERE action_time >" + startTime
				+ " AND action_time < " + endTime + " AND monitor_id in (";

		for (int i = 0; i < monitorId1.size(); i++) {
			sql += "'" + monitorId1.get(i) + "'";

			if (i < monitorId1.size() - 1) {
				sql += ",";
			}
		}

		sql += ")";
		return sqlContext.sql(sql).javaRDD();
	}

	private static JavaPairRDD<String, String> addAreaNameAggregateMonitorId2DetailRDD(
			JavaPairRDD<String, String> aggregateMonitorId2DetailRDD, SQLContext sqlContext) {
		/**
		 * 从数据库中查询出来areaName 与 areaId
		 */
		Boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
		String url = "";
		String user = "";
		String password = "";
		String dbtable = "";

		if (local) {
			url = ConfigurationManager.getProperty(Constants.JDBC_URL);
			user = ConfigurationManager.getProperty(Constants.JDBC_USER);
			password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
		} else {
			url = ConfigurationManager.getProperty(Constants.JDBC_URL_PROD);
			user = ConfigurationManager.getProperty(Constants.JDBC_USER_PROD);
			password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD_PROD);
		}
		Map<String, String> options = new HashMap<>();

		options.put("url", url);
		options.put("user", user);
		options.put("password", password);
		options.put("dbtable", "area_info");

		Dataset<Row> areaInfoDF = sqlContext.read().format("jdbc").options(options).load();
		JavaPairRDD<String, String> areaId2AreaNameRDD = areaInfoDF.javaRDD()
				.mapToPair(new PairFunction<Row, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Row row) throws Exception {
						return new Tuple2<String, String>(row.getString(0), row.getString(1));
					}
				});

		JavaPairRDD<String, String> areaId2DetailRDD = aggregateMonitorId2DetailRDD
				.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<String, String> tuple) throws Exception {
						String infos = tuple._2;
						String areaId = StringUtils.getFieldFromConcatString(infos, "\\|", Constants.FIELD_AREA_ID);
						return new Tuple2<String, String>(areaId, infos);
					}
				});

		return areaId2AreaNameRDD.join(areaId2DetailRDD)
				.mapToPair(new PairFunction<Tuple2<String, Tuple2<String, String>>, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<String, Tuple2<String, String>> tuple) throws Exception {
						String areaId = tuple._1;
						String areaName = tuple._2._1;
						String infos = tuple._2._2;
						infos += "|" + Constants.FIELD_AREA_NAME + "=" + areaName;
						String monitorId = StringUtils.getFieldFromConcatString(infos, "\\|",
								Constants.FIELD_MONITOR_ID);
						return new Tuple2<String, String>(monitorId, infos);
					}
				});
	}

	private static JavaPairRDD<String, String> addAreaNameByBroadCast2AggreageByMnonitor(JavaSparkContext sc,
			JavaPairRDD<String, String> aggregateMonitorId2DetailRDD) {
		// 从数据中查询出来 区域信息
		Map<String, String> areaMap = getAreaInfosFromDB();
		final Broadcast<Map<String, String>> broadcastAreaMap = sc.broadcast(areaMap);

		aggregateMonitorId2DetailRDD.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, String> call(Tuple2<String, String> tuple) throws Exception {
				/**
				 * 从广播变量中取出来区域信息 k:area_id v:area_name
				 */
				Map<String, String> areaMap = broadcastAreaMap.value();
				String monitorId = tuple._1;
				String aggregateInfos = tuple._2;
				String area_id = StringUtils.getFieldFromConcatString(aggregateInfos, "\\|", Constants.FIELD_AREA_ID);
				String area_name = areaMap.get(area_id);

				aggregateInfos += "|" + Constants.FIELD_AREA_NAME + "=" + area_name;
				return new Tuple2<String, String>(monitorId, aggregateInfos);
			}
		});

		return null;
	}

	private static Map<String, String> getAreaInfosFromDB() {
		IAreaDao areaDao = DAOFactory.getAreaDao();

		List<Area> findAreaInfo = areaDao.findAreaInfo();

		Map<String, String> areaMap = new HashMap<>();
		for (Area area : findAreaInfo) {
			areaMap.put(area.getAreaId(), area.getAreaName());
		}
		return areaMap;
	}

	/**
	 * fulAggreageByMonitorRDD key:monitorid value:包含areaName信息
	 * 
	 * @param fulAggreageByMonitorRDD
	 * @param taskParamsJsonObject
	 * @return
	 */
	@SuppressWarnings("resource")
	private static JavaPairRDD<String, String> filterRDDByAreaName(JavaPairRDD<String, String> fulAggreageByMonitorRDD,
			JSONObject taskParamsJsonObject) {
		/**
		 * area_name的获取是在Driver段获取 的 area_name的使用时在Executor端
		 * 可以将area_name放入到广播变量中，然后在Executor中直接从广播变量中获取相应的参数值
		 */

		String area_name = ParamUtils.getParam(taskParamsJsonObject, Constants.FIELD_AREA_NAME);
		/**
		 * 从RDD中获取SparkContext
		 */
		SparkContext sc = fulAggreageByMonitorRDD.context();
		JavaSparkContext jsc = new JavaSparkContext(sc);
		final Broadcast<String> areaNameBroadcast = jsc.broadcast(area_name);

		return fulAggreageByMonitorRDD.filter(new Function<Tuple2<String, String>, Boolean>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean call(Tuple2<String, String> tuple) throws Exception {
				String aggregateInfos = tuple._2;

				String area_name = areaNameBroadcast.value();
				String factAreaName = StringUtils.getFieldFromConcatString(aggregateInfos, "\\|",
						Constants.FIELD_AREA_NAME);
				return area_name.equals(factAreaName);
			}
		});
	}

	/**
	 * 补全区域名
	 * 
	 * @param sqlContext
	 * @param aggregateMonitorId2DetailRDD
	 * @return
	 */
	private static JavaPairRDD<String, String> addAreaName2AggreageByMnonitor(SQLContext sqlContext,
			JavaPairRDD<String, String> aggregateMonitorId2DetailRDD) {
		/***
		 * 准备连接数据的配置信息
		 */
		String url;
		String user;
		String password;
		Boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);

		if (local) {
			url = ConfigurationManager.getProperty(Constants.JDBC_URL);
			user = ConfigurationManager.getProperty(Constants.JDBC_USER);
			password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
		} else {
			url = ConfigurationManager.getProperty(Constants.JDBC_URL_PROD);
			user = ConfigurationManager.getProperty(Constants.JDBC_USER_PROD);
			password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD_PROD);
		}

		Map<String, String> props = new HashMap<>();
		props.put("url", url);
		props.put("user", user);
		props.put("password", password);
		props.put("dbtable", "area_info");

		/**
		 * 将mysql中的area_info表加载到area_Info_DF里面
		 */
		Dataset<Row> area_Info_DF = sqlContext.read().format("jdbc").options(props).load();
		/**
		 * 因为要与我们传入的aggregateMonitorId2DetailRDD进行join join连接的连接的字段是area_id
		 */
		JavaRDD<Row> areaInfosRDD = area_Info_DF.javaRDD();

		JavaPairRDD<String, String> areaId2AreaNameRDD = areaInfosRDD
				.mapToPair(new PairFunction<Row, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Row row) throws Exception {
						String area_id = row.getString(0);
						String area_name = row.getString(1);
						return new Tuple2<String, String>(area_id, area_name);
					}
				});

		JavaPairRDD<String, String> areaId2AggregateInfosRDD = aggregateMonitorId2DetailRDD
				.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<String, String> tuple) throws Exception {
						String aggregateInfos = tuple._2;
						String area_Id = StringUtils.getFieldFromConcatString(aggregateInfos, "\\|",
								Constants.FIELD_AREA_ID);
						return new Tuple2<String, String>(area_Id, aggregateInfos);
					}
				});

		/**
		 * 使用广播变量来代替join join会产生shuffle（有shuffle） = filter + 广播变量 （就不会产生shuffle）
		 */
		return areaId2AreaNameRDD.join(areaId2AggregateInfosRDD)
				.mapToPair(new PairFunction<Tuple2<String, Tuple2<String, String>>, String, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<String, Tuple2<String, String>> tuple) throws Exception {
						String area_name = tuple._2._1;
						String aggregateInfos = tuple._2._2;
						aggregateInfos += "|" + Constants.FIELD_AREA_NAME + "=" + area_name;
						String monitor_Id = StringUtils.getFieldFromConcatString(aggregateInfos, "\\|",
								Constants.FIELD_MONITOR_ID);
						return new Tuple2<String, String>(monitor_Id, aggregateInfos);
					}
				});
	}

	/**
	 * @param sc
	 * @param taskId
	 * @param top10MonitorIds
	 * @param monitor2DetailRDD
	 */
	private static void getMonitorDetails(JavaSparkContext sc, final long taskId, List<String> top5MonitorIds,
			JavaPairRDD<String, Iterable<Row>> monitor2RowsRDD) {

		/**
		 * top10MonitorIds这个集合里面都是monitor_id
		 */
		final Broadcast<List<String>> top5MonitorIdsBroadcast = sc.broadcast(top5MonitorIds);

		/**
		 * 我们想获取每一个卡扣的详细信息，就是从monitor2DetailRDD中取出来包含在top10MonitorIds集合的卡扣的信息
		 */
		monitor2RowsRDD.filter(new Function<Tuple2<String, Iterable<Row>>, Boolean>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
				String monitorId = tuple._1;
				List<String> list = top5MonitorIdsBroadcast.value();
				return list.contains(monitorId);
			}
		}).foreach(new VoidFunction<Tuple2<String, Iterable<Row>>>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
				Iterator<Row> rowsIterator = tuple._2.iterator();
				// 数组长度最好不要写死。。
				Row[] top10Cars = new Row[10];
				while (rowsIterator.hasNext()) {
					Row row = rowsIterator.next();

					long speed = Long.valueOf(row.getString(5));

					for (int i = 0; i < top10Cars.length; i++) {
						if (top10Cars[i] == null) {
							top10Cars[i] = row;
							break;
						} else {
							long _speed = Long.valueOf(top10Cars[i].getString(5));
							if (speed > _speed) {
								for (int j = 9; j > i; j--) {
									top10Cars[j] = top10Cars[j - 1];
								}
								top10Cars[i] = row;
								break;
							}
						}
					}
				}

				IMonitorDAO monitorDAO = DAOFactory.getMonitorDAO();
				List<TopNMonitorDetailInfo> topNMonitorDetailInfos = new ArrayList<>();
				for (Row row : top10Cars) {
					topNMonitorDetailInfos.add(new TopNMonitorDetailInfo(taskId, row.getString(0), row.getString(1),
							row.getString(2), row.getString(3), row.getString(4), row.getString(5), row.getString(6)));
				}
				monitorDAO.insertBatchTop10Details(topNMonitorDetailInfos);
			}
		});
	}

	/**
	 * 1、每一辆车都有speed 按照速度划分是否是高速 中速 普通 低速 2、每一辆车的车速都在一个车速段 对每一个卡扣进行聚合 拿到高速通过
	 * 中速通过 普通 低速通过的车辆各是多少辆 3、四次排序 先按照高速通过车辆数 中速通过车辆数 普通通过车辆数 低速通过车辆数
	 * 
	 * @param cameraRDD
	 * @return
	 */

	private static List<String> speedTopNMonitor(JavaPairRDD<String, Iterable<Row>> groupByMonitorId) {
		/**
		 * key:自定义的类 value：卡扣ID
		 */
		JavaPairRDD<SpeedSortKey, String> speedSortKey2MonitorId = groupByMonitorId
				.mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, SpeedSortKey, String>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<SpeedSortKey, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
						String monitorId = tuple._1;
						Iterator<Row> speedIterator = tuple._2.iterator();

						/**
						 * 这四个遍历 来统计这个卡扣下 高速 中速 正常 以及低速通过的车辆数
						 */
						long lowSpeed = 0;
						long normalSpeed = 0;
						long mediumSpeed = 0;
						long highSpeed = 0;

						while (speedIterator.hasNext()) {
							int speed = StringUtils.convertStringtoInt(speedIterator.next().getString(5));
							if (speed >= 0 && speed < 60) {
								lowSpeed++;
							} else if (speed >= 60 && speed < 90) {
								normalSpeed++;
							} else if (speed >= 90 && speed < 120) {
								mediumSpeed++;
							} else if (speed >= 120) {
								highSpeed++;
							}
						}
						SpeedSortKey speedSortKey = new SpeedSortKey(lowSpeed, normalSpeed, mediumSpeed, highSpeed);
						return new Tuple2<SpeedSortKey, String>(speedSortKey, monitorId);
					}
				});
		/**
		 * key:自定义的类 value：卡扣ID
		 */
		JavaPairRDD<SpeedSortKey, String> sortBySpeedCount = speedSortKey2MonitorId.sortByKey(false);
		/**
		 * 硬编码问题
		 */
		List<Tuple2<SpeedSortKey, String>> take = sortBySpeedCount.take(5);

		List<String> monitorIds = new ArrayList<>();
		for (Tuple2<SpeedSortKey, String> tuple : take) {
			monitorIds.add(tuple._2);
		}
		return monitorIds;
	}

	/**
	 * 按照monitor_id进行聚合，cameraId camer_count|camera_ids|carCount
	 * 
	 * @param monitorId2Detail
	 * @return
	 */
	private static JavaPairRDD<String, String> aggreagteByMonitor(JavaPairRDD<String, Iterable<Row>> monitorId2RowRDD) {
		/**
		 * <monitor_id,List<Row> 集合里面的记录代表的是camera的信息。>
		 */

		/**
		 * 一个monitor_id对应一条记录 为什么使用mapToPair来遍历数据，因为我们要操作的返回值是每一个monitorid
		 * 所对应的详细信息
		 * 
		 * java: 如果返回的RDD希望是一个kv格式的RDD ，那么必须使用 mapToPair
		 * 如何返回的RDD希望是一个非KV格式的RDD，那么必须使用map java(map+mapToPair) = scala(map)
		 * scala: map
		 * 
		 * flatmap map
		 */
		JavaPairRDD<String, String> monitorId2CameraCountRDD = monitorId2RowRDD
				.mapToPair(new PairFunction<Tuple2<String, Iterable<Row>>, String, String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
						String monitorId = tuple._1;
						Iterator<Row> rowIterator = tuple._2.iterator();

						List<String> list = new ArrayList<>();

						StringBuilder tmpInfos = new StringBuilder();

						int count = 0;
						String areaId = "";
						/**
						 * 这个while循环 代表的是这个卡扣一共经过了多少辆车 一辆车的信息就是一个row
						 */
						while (rowIterator.hasNext()) {
							Row row = rowIterator.next();
							areaId = row.getString(7);
							String cameraId = row.getString(2);
							if (!list.contains(cameraId)) {
								list.add(cameraId);
							}
							if (!tmpInfos.toString().contains(cameraId)) {
								tmpInfos.append("," + cameraId);
							}
							count++;
						}
						/**
						 * camera_count
						 */
						int cameraCount = list.size();

						String infos = Constants.FIELD_MONITOR_ID + "=" + monitorId + "|" + Constants.FIELD_AREA_ID
								+ "=" + areaId + "|" + Constants.FIELD_CAMERA_IDS + "="
								+ tmpInfos.toString().substring(1) + "|" + Constants.FIELD_CAMERA_COUNT + "="
								+ cameraCount + "|" + Constants.FIELD_CAR_COUNT + "=" + count;
						return new Tuple2<String, String>(monitorId, infos);
					}
				});
		// <monitor_id,camera_infos(ids,cameracount,carCount)>
		return monitorId2CameraCountRDD;
	}

	private static void saveMonitorState(Long taskId, Accumulator<String> monitorAndCameraStateAccumulator) {
		/**
		 * 累加器中值能在Executor段读取吗？ 不能 这里的读取时在Driver中进行的
		 */
		String accumulatorVal = monitorAndCameraStateAccumulator.value();
		String normalMonitorCount = StringUtils.getFieldFromConcatString(accumulatorVal, "\\|",
				Constants.FIELD_NORMAL_MONITOR_COUNT);
		String normalCameraCount = StringUtils.getFieldFromConcatString(accumulatorVal, "\\|",
				Constants.FIELD_NORMAL_CAMERA_COUNT);
		String abnormalMonitorCount = StringUtils.getFieldFromConcatString(accumulatorVal, "\\|",
				Constants.FIELD_ABNORMAL_MONITOR_COUNT);
		String abnormalCameraCount = StringUtils.getFieldFromConcatString(accumulatorVal, "\\|",
				Constants.FIELD_ABNORMAL_CAMERA_COUNT);
		String abnormalMonitorCameraInfos = StringUtils.getFieldFromConcatString(accumulatorVal, "\\|",
				Constants.FIELD_ABNORMAL_MONITOR_CAMERA_INFOS);

		/**
		 * 这里面只有一条记录
		 */
		MonitorState monitorState = new MonitorState(taskId, normalMonitorCount, normalCameraCount,
				abnormalMonitorCount, abnormalCameraCount, abnormalMonitorCameraInfos);

		IMonitorDAO monitorDAO = DAOFactory.getMonitorDAO();
		monitorDAO.insertMonitorState(monitorState);
	}

	private static JavaPairRDD<String, Row> getMonitor2DetailRDD(JavaRDD<Row> cameraRDD) {
		JavaPairRDD<String, Row> monitorId2Detail = cameraRDD.mapToPair(new PairFunction<Row, String, Row>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Row> call(Row row) throws Exception {
				return new Tuple2<String, Row>(row.getString(1), row);
			}
		});
		return monitorId2Detail;
	}

	@SuppressWarnings("resource")
	private static void getTopNDetails(final long taskId, List<String> topNMonitorIds,
			JavaPairRDD<String, Iterable<Row>> monitor2RowsRDD) {
		/**
		 * 获取车流量排名前N的卡口的详细信息 可以看一下是在什么时间段内卡口流量暴增的。
		 */

		/**
		 * 优化点： 因为topNMonitor2CarFlow
		 * 里面有只有5条数据，将这五条数据封装到广播变量中，然后遍历monitor2DetailRDD ，每遍历一条数据与广播变量中的值作比对。
		 */
		JavaSparkContext jsc = new JavaSparkContext(monitor2RowsRDD.context());
		final Broadcast<List<String>> topNBroadcast = jsc.broadcast(topNMonitorIds);
		monitor2RowsRDD.filter(new Function<Tuple2<String, Iterable<Row>>, Boolean>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
				String monitorId = tuple._1;
				return topNBroadcast.value().contains(monitorId);
			}
		}).foreachPartition(new VoidFunction<Iterator<Tuple2<String, Iterable<Row>>>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Iterator<Tuple2<String, Iterable<Row>>> iterator) throws Exception {
				List<TopNMonitorDetailInfo> monitorDetailInfos = new ArrayList<>();
				while (iterator.hasNext()) {
					Tuple2<String, Iterable<Row>> tuple = iterator.next();
					Iterator<Row> iterator2 = tuple._2.iterator();
					while (iterator2.hasNext()) {
						Row row = iterator2.next();
						monitorDetailInfos.add(new TopNMonitorDetailInfo(taskId, row.getAs("date") + "",
								row.getAs("monitor_id") + "", row.getAs("camera_id") + "", row.getAs("car") + "",
								row.getAs("action_time") + "", row.getAs("speed") + "", row.getAs("road_id") + ""));
					}
				}
				IMonitorDAO monitorDAO = DAOFactory.getMonitorDAO();
				monitorDAO.insertBatchMonitorDetails(monitorDetailInfos);
			}
		});
	}

	/**
	 * 获取卡口流量的前三名，并且持久化到数据库中
	 * 
	 * @param taskId
	 * @param taskParamsJsonObject
	 * @param carCount2MonitorId
	 */
	private static List<String> getTopNMonitorCarFlow(JavaSparkContext sc, long taskId, JSONObject taskParamsJsonObject,
			JavaPairRDD<Integer, String> carCount2MonitorId) {
		/**
		 * 获取车流量排名前三的卡口信息 有什么作用？
		 * 当某一个卡口的流量这几天突然暴增和往常的流量不相符，交管部门应该找一下原因，是什么问题导致的，应该到现场去疏导车辆。
		 */
		int topNumFromParams = Integer.parseInt(ParamUtils.getParam(taskParamsJsonObject, Constants.FIELD_TOP_NUM));

		/**
		 * carCount2MonitorId <carCount,monitor_id>
		 */
		List<Tuple2<Integer, String>> topNCarCount = carCount2MonitorId.sortByKey(false).take(topNumFromParams);

		List<TopNMonitor2CarCount> topNMonitor2CarCounts = new ArrayList<>();
		List<String> topNMonitorIds = new ArrayList<>();
		for (Tuple2<Integer, String> tuple : topNCarCount) {
			TopNMonitor2CarCount topNMonitor2CarCount = new TopNMonitor2CarCount(taskId, tuple._2, tuple._1);
			topNMonitor2CarCounts.add(topNMonitor2CarCount);
			topNMonitorIds.add(tuple._2);
		}

		IMonitorDAO ITopNMonitor2CarCountDAO = DAOFactory.getMonitorDAO();
		ITopNMonitor2CarCountDAO.insertBatchTopN(topNMonitor2CarCounts);

		/**
		 * monitorId2MonitorIdRDD K:monitor_id V:monitor_id 获取topN卡口的详细信息
		 * monitorId2MonitorIdRDD.join(monitorId2RowRDD)
		 */
		return topNMonitorIds;
	}

	/**
	 * 检测卡口状态
	 * 
	 * @param sc
	 * @param cameraRDD
	 */
	private static JavaPairRDD<Integer, String> checkMonitorState(JavaSparkContext sc, SQLContext sqlContext,
			JavaPairRDD<String, String> monitorId2CameraCountRDD, final long taskId, JSONObject taskParamsJsonObject,
			final Accumulator<String> monitorAndCameraStateAccumulator) {
		/**
		 * 从monitor_camera_info表中查询出来每一个卡口对应的camera的数量
		 */
		String sqlText = "SELECT * FROM monitor_camera_info";
		Dataset<Row> standardDF = sqlContext.sql(sqlText);
		JavaRDD<Row> standardRDD = standardDF.javaRDD();
		/**
		 * 使用mapToPair算子将standardRDD变成KV格式的RDD（monitorId2CameraId K:monitor_id
		 * v:camera_id）
		 */
		JavaPairRDD<String, String> monitorId2CameraId = standardRDD.mapToPair(new PairFunction<Row, String, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, String> call(Row row) throws Exception {
				return new Tuple2<String, String>(row.getString(0), row.getString(1));
			}
		});

		/**
		 * 对每一个卡扣下面的信息进行统计，统计出来camera_count（这个卡扣下一共有多少个摄像头）,camera_ids(这个卡扣下，
		 * 所有的摄像头编号拼接成的字符串) 如何来统计？ 1、按照monitor_id分组 2、使用mapToPair遍历，遍历的过程可以统计
		 */
		JavaPairRDD<String, String> standardMonitor2CameraInfos = monitorId2CameraId.groupByKey()
				.mapToPair(new PairFunction<Tuple2<String, Iterable<String>>, String, String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Tuple2<String, String> call(Tuple2<String, Iterable<String>> tuple) throws Exception {
						String monitorId = tuple._1;
						Iterator<String> cameraIterator = tuple._2.iterator();
						int count = 0;
						StringBuilder cameraIds = new StringBuilder();
						while (cameraIterator.hasNext()) {
							cameraIds.append("," + cameraIterator.next());
							count++;
						}
						String cameraInfos = Constants.FIELD_CAMERA_IDS + "=" + cameraIds.toString().substring(1) + "|"
								+ Constants.FIELD_CAMERA_COUNT + "=" + count;
						return new Tuple2<String, String>(monitorId, cameraInfos);
					}
				});

		/**
		 * 将两个RDD进行比较，join leftOuterJoin 为什么使用左外连接？ 左：标准表里面的信息 右：实际信息
		 */
		JavaPairRDD<String, Tuple2<String, Optional<String>>> joinResultRDD = standardMonitor2CameraInfos
				.leftOuterJoin(monitorId2CameraCountRDD);

		JavaPairRDD<Integer, String> carCount2MonitorId = joinResultRDD.mapPartitionsToPair(
				new PairFlatMapFunction<Iterator<Tuple2<String, Tuple2<String, Optional<String>>>>, Integer, String>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Iterator<Tuple2<Integer, String>> call(
							Iterator<Tuple2<String, Tuple2<String, Optional<String>>>> iterator) throws Exception {
						List<Tuple2<Integer, String>> list = new ArrayList<>();
						while (iterator.hasNext()) {
							// 储藏返回值
							Tuple2<String, Tuple2<String, Optional<String>>> tuple = iterator.next();
							String monitorId = tuple._1;
							String standardCameraInfos = tuple._2._1;
							Optional<String> factCameraInfosOptional = tuple._2._2;
							String factCameraInfos = "";

							if (factCameraInfosOptional.isPresent()) {
								factCameraInfos = factCameraInfosOptional.get();
							} else {
								String standardCameraIds = StringUtils.getFieldFromConcatString(standardCameraInfos,
										"\\|", Constants.FIELD_CAMERA_IDS);
								String[] split = standardCameraIds.split(",");
								int abnoramlCameraCount = split.length;
								monitorAndCameraStateAccumulator.add(Constants.FIELD_ABNORMAL_MONITOR_COUNT + "=1|"
										+ Constants.FIELD_ABNORMAL_CAMERA_COUNT + "=" + abnoramlCameraCount + "|"
										+ Constants.FIELD_ABNORMAL_MONITOR_CAMERA_INFOS + "=" + monitorId + ":"
										+ standardCameraIds);
								continue;
							}
							/**
							 * 从实际数据拼接的字符串中获取摄像头数
							 */
							int factCameraCount = Integer.parseInt(StringUtils.getFieldFromConcatString(factCameraInfos,
									"\\|", Constants.FIELD_CAMERA_COUNT));
							/**
							 * 从标准数据拼接的字符串中获取摄像头数
							 */
							int standardCameraCount = Integer.parseInt(StringUtils.getFieldFromConcatString(
									standardCameraInfos, "\\|", Constants.FIELD_CAMERA_COUNT));
							if (factCameraCount == standardCameraCount) {
								/*
								 * 1、正常卡口数量 2、异常卡口数量 3、正常通道（此通道的摄像头运行正常）数
								 * 4、异常卡口数量中哪些摄像头异常，需要保存摄像头的编号
								 */
								monitorAndCameraStateAccumulator.add(Constants.FIELD_NORMAL_MONITOR_COUNT + "=1|"
										+ Constants.FIELD_NORMAL_CAMERA_COUNT + "=" + factCameraCount);
							} else {
								/**
								 * 从实际数据拼接的字符串中获取 摄像编号集合
								 */
								String factCameraIds = StringUtils.getFieldFromConcatString(factCameraInfos, "\\|",
										Constants.FIELD_CAMERA_IDS);

								/**
								 * 从标准数据拼接的字符串中获取摄像头编号集合
								 */
								String standardCameraIds = StringUtils.getFieldFromConcatString(standardCameraInfos,
										"\\|", Constants.FIELD_CAMERA_IDS);

								List<String> factCameraIdList = Arrays.asList(factCameraIds.split(","));
								List<String> standardCameraIdList = Arrays.asList(standardCameraIds.split(","));
								StringBuilder abnormalCameraInfos = new StringBuilder();
								// System.out.println("factCameraIdList:"+factCameraIdList);
								// System.out.println("standardCameraIdList:"+standardCameraIdList);
								int abnormalCmeraCount = 0;
								int normalCameraCount = 0;
								for (String str : standardCameraIdList) {
									if (!factCameraIdList.contains(str)) {
										abnormalCmeraCount++;
										abnormalCameraInfos.append("," + str);
									}
								}
								normalCameraCount = standardCameraIdList.size() - abnormalCmeraCount;
								// 往累加器中更新状态
								monitorAndCameraStateAccumulator.add(Constants.FIELD_NORMAL_CAMERA_COUNT + "="
										+ normalCameraCount + "|" + Constants.FIELD_ABNORMAL_MONITOR_COUNT + "=1|"
										+ Constants.FIELD_ABNORMAL_CAMERA_COUNT + "=" + abnormalCmeraCount + "|"
										+ Constants.FIELD_ABNORMAL_MONITOR_CAMERA_INFOS + "=" + monitorId + ":"
										+ abnormalCameraInfos.toString().substring(1));
							}
							// 从实际数据拼接到字符串中获取车流量
							int carCount = Integer.parseInt(StringUtils.getFieldFromConcatString(factCameraInfos, "\\|",
									Constants.FIELD_CAR_COUNT));
							list.add(new Tuple2<Integer, String>(carCount, monitorId));
						}
						return list.iterator();
					}
				});
		return carCount2MonitorId;
	}
}
