package org.study.hive.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;
import org.junit.Test;

/**
 * 
 * @Title: UDFZodiacAndConstellation
 * @Description:Hive中自定义函数
 * @Author: zhaotf
 * @Since:2018年2月6日 下午1:27:05
 * @see {@linkplain https://www.cnblogs.com/DreamDrive/p/5561113.html}
 */
public class UDFZodiacAndConstellation extends UDF {

	public Text evaluate(Date date, int type) {
		// 这个地方Date的类型是java.sql.Date,和数据库打交道的都是这个.
		// 还可以用DateWritable,这个类面封装了一个java.sql.Date类型.
		java.util.Date uDate = new java.util.Date(date.getTime());
		if (type == 0) {// 生肖
			return new Text(getZodica(uDate));
		} else if (type == 1) {// 星座
			return new Text(getConstellation(uDate));
		} else {
			return new Text("NULL");
		}
	}

	public final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };
	public final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座",
			"射手座", "魔羯座" };
	public final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

	/**
	 * 根据日期获取生肖
	 */
	public String getZodica(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return zodiacArr[cal.get(Calendar.YEAR) % 12];
	}

	/**
	 * 根据日期获取星座
	 */
	public String getConstellation(java.util.Date date) {
		if (date == null) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
		}
		if (month >= 0) {
			return constellationArr[month];
		}
		// default to return 魔羯
		return constellationArr[11];
	}

	/**
	 * 测试方法
	 */
	@Test
	public void test() throws Exception {
		System.out.println(getConstellation(new SimpleDateFormat("yyyy-MM-dd").parse("2016-07-09")));
		System.out.println(getConstellation(new SimpleDateFormat("yyyy-MM-dd").parse("2016-07-31")));
		System.out.println(getZodica(new SimpleDateFormat("yyyy-MM-dd").parse("2016-07-31")));
	}

}
