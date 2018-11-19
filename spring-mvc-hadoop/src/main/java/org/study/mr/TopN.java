package org.study.mr;

import java.io.IOException;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @see {@linkplain https://www.cnblogs.com/zmt0429/p/6703504.html
 *      MapReduce编程初步（WordCount，TopN）}
 * @Title: TopN
 * @Description: topN计算
 * @Author: zhaotf
 * @Since:2018年11月19日 下午1:49:13
 */
public class TopN {

	/**
	 * 
	 * @Title: MyMapper
	 * @Description:map端
	 * @Author: zhaotf
	 * @Since:2018年11月19日 下午1:51:43
	 */
	public static class MyMapper extends Mapper<Object, Text, NullWritable, IntWritable> {
		private TreeMap<Integer, Integer> tree = new TreeMap<Integer, Integer>();// 排序器

		@Override
		protected void setup(Context context) {
			System.out.println(
					context.getJobID() + ":Mapper(" + context.getConfiguration().getInt("N", 10) + "):in setup...");
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			System.out.println(
					context.getJobID() + ":Mapper(" + context.getConfiguration().getInt("N", 10) + "):in cleanup...");
			for (Integer val : tree.values()) {
				context.write(NullWritable.get(), new IntWritable(val));
			}
		}

		@Override
		public void map(Object key, Text value, Context context) {
			String keyNum = value.toString();
			int num = Integer.parseInt(keyNum);
			tree.put(num, num);
			if (tree.size() > context.getConfiguration().getInt("N", 10)) {
				tree.remove(tree.firstKey());
			}
		}
	}

	/**
	 * 
	 * @ClassName: MyReducer
	 * @Description:reduce端
	 * @author: zhaotf
	 * @date: 2018年11月19日 下午4:26:11
	 */
	public static class MyReducer extends Reducer<NullWritable, IntWritable, NullWritable, IntWritable> {
		private TreeMap<Integer, Integer> tree = new TreeMap<Integer, Integer>();// 排序器

		@Override
		public void reduce(NullWritable key, Iterable<IntWritable> values, Context context) {
			for (IntWritable val : values) {
				tree.put(val.get(), val.get());
				if (tree.size() > context.getConfiguration().getInt("N", 10)) {
					tree.remove(tree.firstKey());
				}
			}
		}

		@Override
		protected void cleanup(Context context) throws IOException, InterruptedException {
			for (Integer val : tree.descendingKeySet()) {
				context.write(NullWritable.get(), new IntWritable(val));
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		// String[] otherArgs = new GenericOptionsParser(conf,
		// args).getRemainingArgs();
		// if (otherArgs.length < 3) {
		// System.err.println("heheda");
		// System.exit(2);
		// }
		// conf.setInt("N", new Integer(otherArgs[0]));
		// System.out.println("N:" + otherArgs[0]);

		Job job = Job.getInstance(conf, "TopN");
		job.setJarByClass(TopN.class);
		job.setMapperClass(MyMapper.class);
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(MyReducer.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(IntWritable.class);

		// 数据源、输出结果目录
		FileInputFormat.setInputPaths(job, "/data/mapReduce/TopN");
		FileOutputFormat.setOutputPath(job, new Path("/data/mapReduce/TopN-rslt" + System.currentTimeMillis()));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
