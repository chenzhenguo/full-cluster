package com.hhcf.backend.spark.mllib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * 
 * @Title: LocalLinearRegression
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月19日 下午5:36:08
 * @see {@linkplain http://blog.selfup.cn/747.html}
 */
public class LocalLinearRegression {

	public static void main(String[] args) {
//		System.out.println("aaaaaaaaaaaaa");
		SparkConf conf=new SparkConf().setAppName("Regression").setMaster("local[2]");
		JavaSparkContext sc = new JavaSparkContext(conf);
//		
		JavaRDD<String> data = sc.textFile("");
//		data.map(new MapFunction(){});
		
		sc.stop();
	}
}
