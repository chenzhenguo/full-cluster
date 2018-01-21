package com.hhcf.backend.spark.mllib;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.*;
import scala.Tuple2;

/**
 * @Title: LocalLinearRegression
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月19日 下午5:36:08
 * @see {@linkplain http://blog.selfup.cn/747.html}
 */
public class LocalLinearRegression {

	public static void main(String[] args) {
		// System.out.println("aaaaaaaaaaaaa");
		SparkConf conf = new SparkConf().setAppName("Regression").setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		//
//		JavaRDD<String> data = sc.textFile("‪D:\\data\\spark\\mllib-lpsa-data.txt");
		JavaRDD<String> data = sc.textFile("‪D:/data/spark/lpsa.txt");
		JavaRDD<LabeledPoint> parsedData = data.map((String line) -> {
			// 文本转数值
			String[] parts = line.split(",");
			double ds[] = Arrays.stream(parts[1].split(" ")).mapToDouble(Double::parseDouble).toArray();
			LabeledPoint point = new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(ds));
			System.out.println("aa:"+point.productIterator());
			return point;
		}).cache();

		int numIterations = 100; // 迭代次数
		LinearRegressionModel model = LinearRegressionWithSGD.train(parsedData.rdd(), numIterations);
		RidgeRegressionModel model1 = RidgeRegressionWithSGD.train(parsedData.rdd(), numIterations);
		LassoModel model2 = LassoWithSGD.train(parsedData.rdd(), numIterations);

		print(parsedData, model);
		print(parsedData, model1);
		print(parsedData, model2);

		// 预测一条新数据方法
		double[] da = new double[] { 1.0, 1.0, 2.0, 1.0, 3.0, -1.0, 1.0, -2.0 };
		Vector ve = Vectors.dense(da);
		System.out.println("测试结果:" + model.predict(ve) + "," + model1.predict(ve) + "," + model2.predict(ve));

		sc.stop();
	}

	public static void print(JavaRDD<LabeledPoint> parseData, GeneralizedLinearModel model) {
		JavaPairRDD<Double, Double> vap = parseData.mapToPair(point -> {
			double prediction = model.predict(point.features());// 用模型预测训练数据
			return new Tuple2<>(point.label(), prediction);
		});
		Double mse = vap.mapToDouble((Tuple2<Double, Double> t) -> Math.pow(t._1() - t._2(), 2)).mean();// 计算预测值与实际值差值的平方值的均值
		System.out.println(model.getClass().getName() + " training Mean Squared Error = " + mse);
	}

}
