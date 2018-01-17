package com.hhcf.study.hadoop.mapreduce;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @Title: AvgScore
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月17日 下午5:41:46
 * @see {@linkplain http://blog.csdn.net/liuc0317/article/details/8716368}
 */
public class AvgScore implements Tool {
	public static final Logger log = LoggerFactory.getLogger(AvgScore.class);
	Configuration configuration;

	@Override
	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConf(Configuration arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int run(String[] args) throws Exception {
		Job job = new Job(getConf());
		job.setJarByClass(AvgScore.class);
		job.setJobName("avgscore");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setMapperClass(MyMap.class);
		job.setCombinerClass(MyReduce.class);
		job.setReducerClass(MyReduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		boolean success = job.waitForCompletion(true);

		return success ? 0 : 1;

	}

	public static class MyMap extends Mapper<Object, Text, Text, IntWritable> {
		Configuration config = HBaseConfiguration.create();// 获取hbase 的操作上下文
		private static IntWritable linenum = new IntWritable(1);// 初始化一个变量值

		@Override
		protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String stuInfo = value.toString();
			System.out.println("studentInfo:" + stuInfo);
			log.info("MapSudentInfo:" + stuInfo);
			StringTokenizer tokenizerArticle = new StringTokenizer(stuInfo, "\n");
			while (tokenizerArticle.hasMoreTokens()) {
				StringTokenizer tokenizer = new StringTokenizer(tokenizerArticle.nextToken());
				String name = tokenizer.nextToken();
				String score = tokenizer.nextToken();
				Text stu = new Text(name);
				int intscore = Integer.parseInt(score);
				log.info("MapStu:" + stu.toString() + " " + intscore);
				context.write(stu, new IntWritable(intscore)); // zs 90
				// create 'stu','name','score'
				HTable table = new HTable(config, "stu");
				byte[] row1 = Bytes.toBytes("name" + linenum);
				Put p1 = new Put(row1);
				byte[] databytes = Bytes.toBytes("name");
				p1.add(databytes, Bytes.toBytes("1"), Bytes.toBytes(name));
				table.put(p1);// put 'stu','name','name:1','zs'
				table.flushCommits();

				byte[] row2 = Bytes.toBytes("score" + linenum);
				Put p2 = new Put(row2);
				byte[] databytes2 = Bytes.toBytes("score");
				p2.add(databytes2, Bytes.toBytes("1"), Bytes.toBytes(score));
				table.put(p2);// put 'stu','score','score:1','90'
				table.flushCommits();
				linenum = new IntWritable(linenum.get() + 1);// 对变量值进行变值处理
			}
		}
	}

	public static class MyReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			int count = 0;
			Iterator<IntWritable> iterator = values.iterator();
			while (iterator.hasNext()) {
				sum += iterator.next().get();
				count++;
			}
			int avg = (int) sum / count;
			context.write(key, new IntWritable(avg));
		}
	}

}
