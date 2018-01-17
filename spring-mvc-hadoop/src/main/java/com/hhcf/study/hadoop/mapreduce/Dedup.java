package com.hhcf.study.hadoop.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 * @Title: Dedup
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月17日 下午5:33:49
 * @see {@linkplain http://blog.csdn.net/admin1973/article/details/62037603?locationNum=6&fps=1}
 */
public class Dedup {
	private final static String HADOOP_IN_URL = "hdfs://192.168.159.131:9000/dedup_in";
	private final static String HADOOP_OUT_URL = "hdfs://192.168.159.131:9000/dedup_out";
	
	// map将输入中的value复制到输出数据的key上,并直接输出
	public static class Map extends Mapper<Object, Text, Text, Text> {
		private static Text line = new Text();// 每行数据
		// 实现map函数

		@Override
		protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			line = value;
			context.write(line, new Text(""));
		}
	}

	// reduce将输入中的key复制到输出数据的key上,并直接输出
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		// 实现reduce函数

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			context.write(key, new Text(""));
		}
	}

	public static void main(String[] args) throws Exception {
		// System.setProperty("hadoop.home.dir", "F:\\hadoop-2.7.3");
		// Configuration configuration = new Configuration();
		// //这句话很关键
		// configuration.set("mapred.job.tracker","192.168.113.130:9001");
		// String[] ioArgs = new String[]{"dedup_in","dedup_out"};
		// String[] otherArgs = new
		// GenericOptionsParser(configuration,ioArgs).getRemainingArgs();
		// if(otherArgs.length!=2){
		// System.err.println("Usage: Data Deduplication <in> <out>");
		// System.exit(2);
		// }
//		// 输入路径
//		String dst = "hdfs://192.168.113.130:9000/dedup_in";
//		// 输出路径，必须是不存在的，空文件夹也不行。
//		String dstOut = "hdfs://192.168.113.130:9000/dedup_out";
		Configuration configuration = new Configuration();
		// System.setProperty("hadoop.home.dir", "F:\\hadoop-2.7.3");
		System.setProperty("hadoop.home.dir", "D:\\usr\\local\\software\\hadoop-2.8.2");
		configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
		configuration.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

		Job job = new Job(configuration, "Data Deduplication");
		job.setJarByClass(Dedup.class);
		// 设置Map,Combine和Reduce处理类
		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		// 设置输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// 设置输出目录
		FileInputFormat.addInputPath(job, new Path(HADOOP_IN_URL));
		FileOutputFormat.setOutputPath(job, new Path(HADOOP_OUT_URL));

		System.exit(job.waitForCompletion(true) ? 0 : 1);

	}
}
