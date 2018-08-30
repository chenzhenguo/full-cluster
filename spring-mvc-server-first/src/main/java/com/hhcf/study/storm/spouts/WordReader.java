package com.hhcf.study.storm.spouts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * @Title: WordReader
 * @Description:
 * @Author: zhaotf
 * @Since:2018年8月30日 下午2:02:54
 * @see {@linkplain https://blog.csdn.net/qq_37095882/article/details/77624246}
 */
public class WordReader implements IRichSpout {
	private boolean completed = false;
	private FileReader fileReader;
	private SpoutOutputCollector collector;
	private Map map;
	private TopologyContext context;

	@Override
	public void ack(Object msgId) {
		System.out.println("ack-OK:" + msgId);
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Object msgId) {
		System.out.println("失败-fail-FAIL:" + msgId);
	}

	@Override
	public void nextTuple() {
		// 这个方法会不断的被调用，直到整个文件都读完了，我们将等待并返回。
		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// 什么也不做
			}
			return;
		}
		String str;
		// 创建reader
		BufferedReader reader = new BufferedReader(fileReader);
		try {
			// 读所有文本行
			while ((str = reader.readLine()) != null) {
				// 按行发布一个新值
				this.collector.emit(new Values(str), str);
			}
		} catch (Exception e) {
			throw new RuntimeException("异常-bb-Error reading tuple", e);
		} finally {
			completed = true;
		}
	}

	@Override
	public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
		this.map = map;
		this.context = context;
		this.collector = collector;
		try {
			this.fileReader = new FileReader(map.get("wordsFile").toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("异常-aaa-Error reading file [" + map.get("wordFile") + "]");
		}
	}

	/**
	 * 声明输入域"word"
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
