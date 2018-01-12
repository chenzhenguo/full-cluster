package com.hhcf.study.full.util;

/**
 * 
 * @Title: FullTestUtils
 * @Description:
 * @Author: zhaotf
 * @Since:2018年1月4日 下午4:14:56
 */
public class FullTestUtils {

	public static void main(String[] args) {
		hashcodeTest();
	}

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
		System.out.println("yangcq的hashcode = " + hashcode); // yangcq的hashcode=-737879313
		System.out.println("yangcq的hashcode = " + str.hashCode()); // yangcq的hashcode=-737879313

	}

}
