package org.study.spark.spark.test;

import java.util.Arrays;

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;

public class SparkAccumulator {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf()
				.setAppName("SparkAccumulator")
				.setMaster("local[2]");
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		
		final Accumulator<String> accumulator = sc.accumulator("A", new UserDefinedAccumulator());
		
		JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1,2,3,4,5,6,7,8,9,10),2);
		rdd.foreach(new VoidFunction<Integer>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Integer item) throws Exception {
				accumulator.add(""+item);
			}
		});
		
		System.out.println(accumulator.value());
		sc.stop();
		
	}
}
