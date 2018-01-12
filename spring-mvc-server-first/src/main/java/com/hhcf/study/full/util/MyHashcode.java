package com.hhcf.study.full.util;

/**
 * 
 * @Title: MyHashcode
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月4日 下午4:17:18
 * @see {@linkplain http://blog.csdn.net/reggergdsg/article/details/53819293}
 */
public class MyHashcode {

	/** The value is used for character storage. */
	static char value[];

	/** The offset is the first index of the storage that is used. */
	static int offset;

	/** The count is the number of characters in the String. */
	static int count;

	/** Cache the hash code for the string */
	static int hash; // Default to 0

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str1 = new String("yangcq");
		String str2 = new String("yangcq");
		// 如果2个字符串的内容相同，那么这2个字符串的hashcode必然相同
		System.out.println(new String("yangcq").hashCode() == new String("yangcq").hashCode());
		System.out.println(str1.hashCode() == str2.hashCode());

		System.out.println(str1.hashCode());
		System.out.println(hashCode1(str1));

		// 测试自定义的hashcode方法
		HashcodeOfString hashcodeOfString = new HashcodeOfString();
		hashcodeOfString.hashCode(str1);
		System.out.println("str1的hashcode = " + hashcodeOfString.hashCode(str1));
		System.out.println("str1的hashcode = " + str1.hashCode());
	}

	// HashMap中实现的hash算法(再hash算法)
	static int hash(int h) {
		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	// String类实现的hashcode方法源代码
	static int hashCode1(String str) {
		int h = hash;
		if (h == 0) {
			int off = 0;
			char val[] = str.toCharArray();
			int len = str.length();

			for (int i = 0; i < len; i++) {
				h = 31 * h + val[off++];
			}
			hash = h;
		}
		return h;
	}

	// String类的hashcode值（哈希值）是如何计算得到的？具体实现？为了方便阅读，我们来进行分步说明
	static void hashcodeTest() {

		String str = "yangcq";

		// 第一步 = (int)'y'
		// 第二步 = (31 * (int)'y') + (int)'a'
		// 第三步 = 31 * ((31 * (int)'y') + (int)'a') + (int)'n'
		// 第四步 = 31 * (31 * ((31 * (int)'y') + (int)'a') + (int)'n') + (int)'g'
		// 第五步 = 31 * (31 * (31 * ((31 * (int)'y') + (int)'a') + (int)'n') +
		// (int)'g') + (int)'c'
		// 第六步 = 31 * (31 * (31 * (31 * ((31 * (int)'y') + (int)'a') + (int)'n')
		// + (int)'g') + (int)'c') + (int)'q'

		// 上面的过程，也可以用下面的方式表示

		// 第一步 = (int)'y'
		// 第二步 = 31 * (第一步的计算结果) + (int)'a'
		// 第三步 = 31 * (第二步的计算结果) + (int)'n'
		// 第四步 = 31 * (第三步的计算结果) + (int)'g'
		// 第五步 = 31 * (第四步的计算结果) + (int)'c'
		// 第六步 = 31 * (第五步的计算结果) + (int)'q'

		int hashcode = 31 * (31 * (31 * (31 * ((31 * (int) 'y') + (int) 'a') + (int) 'n') + (int) 'g') + (int) 'c')
				+ (int) 'q';
		System.out.println("yangcq的hashcode = " + hashcode); // yangcq的hashcode
																// = -737879313
		System.out.println("yangcq的hashcode = " + str.hashCode()); // yangcq的hashcode
																	// =
																	// -737879313

	}
}
