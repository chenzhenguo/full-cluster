package org.study.storm;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.storm.shade.org.apache.commons.io.FileUtils;
import org.apache.storm.shade.org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * @Title: SensitiveFileReader
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 下午2:04:48
 * @see {@linkplain https://www.cnblogs.com/jietang/p/5423438.html}
 */
public class SensitiveFileReader extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	// 福州地市用户敏感短信文件上传路径
	public static final String InputFuZhouPath = "/home/tj/data/591";
	// 厦门地市用户敏感短信文件上传路径
	public static final String InputXiaMenPath = "/home/tj/data/592";
	// 处理成功改成bak后缀
	public static final String FinishFileSuffix = ".bak";
	private String sensitiveFilePath = "";
	private SpoutOutputCollector collector;

	public SensitiveFileReader(String sensitiveFilePath) {
		this.sensitiveFilePath = sensitiveFilePath;
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		Collection<File> files = FileUtils.listFiles(new File(sensitiveFilePath),
				FileFilterUtils.notFileFilter(FileFilterUtils.suffixFileFilter(FinishFileSuffix)), null);

		for (File f : files) {
			try {
				List<String> lines = FileUtils.readLines(f, "GBK");
				for (String line : lines) {
					System.out.println("[SensitiveTrace]:" + line);
					collector.emit(new Values(line));
				}
				FileUtils.moveFile(f, new File(f.getPath() + System.currentTimeMillis() + FinishFileSuffix));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sensitive"));
	}

}
