package com.hhcf.backend.spark.sql;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

/**
 * 
 * @Title: LocalSparkSql.java
 * @Description: TODO Spark SQL入门用法与原理分析
 * @author zhaotf
 * @date 2018年1月21日 下午6:17:13
 * @see {@linkplain http://blog.csdn.net/silviakafka/article/details/54091005}
 */
public class LocalSparkSql {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("").setMaster("local[2]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sc);

		Dataset<Row> df = sqlContext.read().json("/testDir/people.json");
 		df.show();
 		df.printSchema();
 		df.registerTempTable("tb");

 		String sqlText = "SELECT age,COUNT(0) as CountAGE FROM tb GROUP BY age";
 		sqlContext.sql(sqlText);

		sc.stop();
	}
	
}
