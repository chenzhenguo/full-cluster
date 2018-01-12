package com.hhcf.study.full.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @Title: TestMd5AndSha1
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月4日 下午4:20:01
 * @see {@linkplain https://www.cnblogs.com/xrq730/p/5186728.html}
 */
public class TestMd5AndSha1 {

	public static String md5(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(data.getBytes());
		StringBuffer buf = new StringBuffer();
		byte[] bits = md.digest();
		for (int i = 0; i < bits.length; i++) {
			int a = bits[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));
		}
		return buf.toString();
	}

	public static String sha1(String data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		md.update(data.getBytes());
		StringBuffer buf = new StringBuffer();
		byte[] bits = md.digest();
		for (int i = 0; i < bits.length; i++) {
			int a = bits[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));
		}
		return buf.toString();
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		int shardSize = 5;// 分片数,取余时要取绝对值
		String data = "abc";
		// MD5
		System.out.println(
				"MD5 : " + md5(data) + "," + md5(data).hashCode() + "," + Math.abs(md5(data).hashCode() % shardSize));
		// SHA1
		System.out.println("SHA1 : " + sha1(data) + "," + sha1(data).hashCode() + ","
				+ Math.abs(sha1(data).hashCode() % shardSize));

		System.out.println("abc:" + "abc".hashCode() % shardSize);
		System.out.println("192.168.0.0:111的哈希值：" + "192.168.0.0:1111".hashCode() % shardSize);
		System.out.println("192.168.0.1:111的哈希值：" + "192.168.0.1:1111".hashCode() % shardSize);
		System.out.println("192.168.0.2:111的哈希值：" + "192.168.0.2:1111".hashCode() % shardSize);
		System.out.println("192.168.0.3:111的哈希值：" + "192.168.0.3:1111".hashCode() % shardSize);
		System.out.println("192.168.0.4:111的哈希值：" + "192.168.0.4:1111".hashCode() % shardSize);
	}

}
