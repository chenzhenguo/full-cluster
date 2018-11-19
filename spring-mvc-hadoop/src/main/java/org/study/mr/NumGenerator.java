package org.study.mr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * @see {@linkplain https://www.cnblogs.com/zmt0429/p/6703504.html
 *      MapReduce编程初步（WordCount，TopN）}
 * @Title: NumGenerator
 * @Description:TopN，数据准备
 * @Author: zhaotf
 * @Since:2018年11月19日 上午11:41:35
 */
public class NumGenerator {

	public static void main(String[] args) throws Exception {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		Random random = new Random();
		try {
			String fileName = "/data/mapReduce/TopN/random_num";
			for (int i = 0; i < 10; i++) {
				String tmpName = fileName + "-" + i + ".txt";
				File file = new File(tmpName);
				fos = new FileOutputStream(file);
				osw = new OutputStreamWriter(fos, "utf-8");
				bw = new BufferedWriter(osw);
				for (int j = 0; j < 1000; j++) {
					int rm = random.nextInt();
					System.out.println("aaa:" + rm + "," + file.getName());
					bw.write(rm + "");
					bw.newLine();
				}
				bw.flush();
				System.out.println(i + ":Complete.");

				fos.close();
				osw.close();
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
