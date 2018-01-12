package com.hhcf.study.full.util;

import java.io.Serializable;

/**
 * 
 * @Title: HashcodeOfString
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月4日 下午4:18:19
 * @see {@linkplain http://blog.csdn.net/reggergdsg/article/details/53819293}
 */
public class HashcodeOfString implements Serializable {
	private static final long serialVersionUID = 1L;

	public int hashCode(String str) {
		// 最终计算得出的哈希值，转化为int以后的哈希值
		int hashcode = 0;
		// 临时哈希值变量
		int hash = 0;
		if (hash == 0) {
			// 当前char的索引
			int off = 0;
			// 字符串str的字符数组表示
			char val[] = str.toCharArray();
			// 字符串str的长度
			int len = str.length();
			for (int i = 0; i < len; i++) {
				hash = 31 * hash + val[off++];
			}
			hashcode = hash;
		}
		return hashcode;
	}

	public static void main(String[] args) {
		System.out.println("Java取余 (%):" + 139 % 5);
		System.out.println("Java取模:" + 139 / 5);
		System.out.println("Java取余 (%):" + 19 % 5);
		System.out.println("Java取模:" + 19 / 5);
		System.out.println("Java取余 (%):" + 5 % 5);
		System.out.println("Java取模:" + 5 / 5);
		System.out.println("Java取余 (%):" + 1 % 5);
		System.out.println("Java取模:" + 1 / 5);
		System.out.println("Java取余 (%):" + 0 % 5);
		System.out.println("Java取模:" + 0 / 5);
	}

}
