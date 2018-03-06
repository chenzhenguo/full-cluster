package org.study.bjsxt.spark.areaRoadFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;


import scala.Tuple2;

public class BroadcastMap {
	public static void main(String[] args) {
		// 创建SparkConf
		SparkConf conf = new SparkConf()
				.setAppName("BroadcastMap");
		 conf.setMaster("local");
		
		// 构建Spark上下文
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		
		List<Tuple2<Integer, String>> nameList = Arrays.asList(
				new Tuple2<Integer, String>(1,"Angelababy"),
				new Tuple2<Integer, String>(2,"dilibaba"),
				new Tuple2<Integer, String>(3,"baihe")
				);
		List<Tuple2<Integer, Integer>> scoreList = Arrays.asList(
				new Tuple2<Integer, Integer>(1,100),
				new Tuple2<Integer, Integer>(2,80),
				new Tuple2<Integer, Integer>(3,30),
				new Tuple2<Integer, Integer>(4,130)
				);
		
		JavaPairRDD<Integer, String> nameRDD = sc.parallelizePairs(nameList);
		JavaPairRDD<Integer, Integer> scoreRDD = sc.parallelizePairs(scoreList);
		
		
		JavaPairRDD<Integer, Tuple2<String, Integer>> join = nameRDD.join(scoreRDD);
		
		List<Tuple2<Integer, String>> nameLists = nameRDD.collect();
		
		Map<Integer, String> nameMap = new HashMap<>();
		for (Tuple2<Integer, String> tuple2 : nameLists) {
			nameMap.put(tuple2._1, tuple2._2);
		}
		
		final Broadcast<Map<Integer, String>> mapBroadcast = sc.broadcast(nameMap);
		
		scoreRDD.flatMapToPair(new PairFlatMapFunction<Tuple2<Integer,Integer>, Integer,Tuple2<String,Integer>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<Tuple2<Integer, Tuple2<String, Integer>>> call(Tuple2<Integer, Integer> tuple) throws Exception {
				List<Tuple2<Integer, Tuple2<String, Integer>>> list = new ArrayList<>();
				Map<Integer, String> map = mapBroadcast.value();
				Integer id = tuple._1;
				Integer score = tuple._2;
				String name = map.get(id);
				
				if(name != null){
					list.add(new Tuple2<Integer, Tuple2<String,Integer>>(id, new Tuple2<String,Integer>(name, score)));
				}
				return list.iterator();
			}
		}).foreach(new VoidFunction<Tuple2<Integer,Tuple2<String,Integer>>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(Tuple2<Integer, Tuple2<String, Integer>> t) throws Exception {
				 System.out.println(t);
			}
		});
		
	}
}
