package org.study.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * @Title: SparkJavaApiTest1
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 上午8:09:21
 * @see {@linkplain http://blog.csdn.net/t1dmzks/article/details/70234272}
 */
public class SparkJavaApiTest1 {

	private static JavaSparkContext context;

	/**
	 * @see aa bb cc aa aa aa dd dd ee ee ee ee
	 * @see ff aa bb zks
	 * @see ee kks
	 * @see ee zz zks
	 */
	public static void main(String[] args) {
		SparkConf sparkConf = new SparkConf().setAppName("sparkAPI1").setMaster("local[2]");
		context = new JavaSparkContext(sparkConf);
		JavaRDD<String> lines = context.textFile("F:\\sparktest\\sample.txt", 1);
		
		// 输入的是一个string的字符串，输出的是一个(String, Integer) 的map
		JavaPairRDD<String, Integer> pairRDD = lines.mapToPair(new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<String, Integer> call(String t) throws Exception {
				return new Tuple2<String, Integer>(t.split("\\s+")[0], 1);
			}
		});

		// java版本 spark2.0以上
		JavaPairRDD<String, Integer> wordPairRDD = lines
				.flatMapToPair(new PairFlatMapFunction<String, String, Integer>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Iterator<Tuple2<String, Integer>> call(String t) throws Exception {
						List<Tuple2<String, Integer>> list = new ArrayList<Tuple2<String, Integer>>();
						String[] split = t.split("\\s+");
						for (String s : split) {
							Tuple2<String, Integer> tp = new Tuple2<String, Integer>(s, 1);
							list.add(tp);
						}
						return list.iterator();
					}
				});

	}
	
	public  void distinct() {
		JavaRDD<String> rdd1 = context.parallelize(Arrays.asList("aa","aa","bb","cc","dd"));
		JavaRDD<String> rdd2 =rdd1.distinct();
		List<String> list = rdd2.collect();
	}
	

}
