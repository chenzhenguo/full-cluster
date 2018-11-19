package org.study.mr;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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
 * @see {@linkplain https://blog.csdn.net/qifuchenluo/article/details/70241023
 *      java实现map-reduce代码}
 * @Title: NbRoute
 * @Description:wordCount，代码有问题，结果文件中无内容，待调查
 * @Author: zhaotf
 * @Since:2018年11月19日 上午9:24:44
 */
public class NbRoute implements Tool {
	private static Logger logger = LoggerFactory.getLogger(NbRoute.class);
	private Configuration configuration;

	public static void main(String[] args) throws Exception {
		int ret = ToolRunner.run(new NbRoute(), args);
		System.out.println("eee:" + ret);
		logger.info("bbb:" + ret);
		System.exit(ret);
	}

	/**
	 * 
	 * @Title: MyMap
	 * @Description:map端
	 * @Author: zhaotf
	 * @Since:2018年11月19日 上午9:34:55
	 */
	public static class MyMap extends Mapper<LongWritable, Text, Text, Text> {
		@Override
		public void map(LongWritable lw, Text text, Context context) throws IOException, InterruptedException {
			String[] citation = text.toString().split(",");
			context.write(new Text(citation[0]), new Text(citation[1]));
		}
	}

	/**
	 * 
	 * @Title: MyReduce
	 * @Description:reduce端
	 * @Author: zhaotf
	 * @Since:2018年11月19日 上午9:35:35
	 */
	public static class MyReduce extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			String csv = "";
			for (Text val : values) {
				if (csv.length() > 0) {
					csv += ",";
				}
				if (!csv.contains(val.toString())) {
					csv += val.toString();
				}
			}
			String[] tmp = csv.split(",");
			Arrays.sort(tmp);
			String scenic = "";
			if (tmp.length > 2) {
				for (int i = 0; i < tmp.length; i++) {
					scenic += tmp[i];
					scenic += ",";
				}
				String sctmp = scenic.substring(0, scenic.length() - 1);
				context.write(key, new Text(sctmp));
			}
		}
	}

	@Override
	public int run(String[] arg0) throws Exception {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "NbRoute");
		job.setJarByClass(NbRoute.class);
		job.setJobName("NbRoute");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		// map/Reduce端配置
		job.setMapperClass(MyMap.class);
		job.setCombinerClass(MyReduce.class);
		job.setReducerClass(MyReduce.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		// 数据源、结果目录设置
		// FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		// FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
//		FileInputFormat.setInputPaths(job, new Path("/data/mapReduce/wordCount-1"));
		FileInputFormat.setInputPaths(job, "/data/mapReduce/wordCount-1");
		FileOutputFormat.setOutputPath(job, new Path("/data/mapReduce/wordCount-1-rslt"));

		boolean success = job.waitForCompletion(true);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		return success ? 0 : 1;
	}

	@Override
	public Configuration getConf() {
		return this.configuration;
	}

	@Override
	public void setConf(Configuration arg0) {
		this.configuration = new Configuration();
		this.configuration = arg0;
	}

}
