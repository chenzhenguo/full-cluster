package com.hhcf.backend;

import org.junit.Test;

/**
 * 
 * @ClassName: OtherUtils
 * @Description:
 * @author: zhaotf
 * @date: 2018年11月20日 上午10:27:59
 */
public class OtherUtils {

	@Test
	public void test1() {
		// System.out.println("aaaaaaaaaa");
		String v1 = "aaa";
		String v2 = "k2";
		String text = StringUtils.setFieldInConcatString(v1, "\\|", v2, "3");
		System.out.println("aaa:" + text);
	}

}
