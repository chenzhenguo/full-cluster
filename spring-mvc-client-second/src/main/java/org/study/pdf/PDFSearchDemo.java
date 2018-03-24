package org.study.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * @Title: PDFSearchDemo
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月24日 下午4:17:02
 * @see {@linkplain https://zhidao.baidu.com/question/1735694132291125067.html}
 */
public class PDFSearchDemo {
	private static String KEY_WORD = "借款人";
	private static int i = 0; // 
	static List<float[]> arrays = new ArrayList<float[]>();
	static String sb;

	private static List<float[]> getKeyWords(String filePath) {
		try {
			PdfReader pdfReader = new PdfReader(filePath);
			int pageNum = pdfReader.getNumberOfPages();
			PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
			for (i = 1; i < 2; i++) {
				pdfReaderContentParser.processContent(i, new RenderListener() {
					@Override
					public void renderText(TextRenderInfo textRenderInfo) {
						String text = textRenderInfo.getText(); // 整页内容

						if (null != text && text.contains(KEY_WORD)) {
							com.itextpdf.awt.geom.Rectangle2D.Float boundingRectange = textRenderInfo.getBaseline()
									.getBoundingRectange();
							sb = boundingRectange.x + "--" + boundingRectange.y + "---";

							float[] resu = new float[3];
							resu[0] = boundingRectange.x;
							resu[1] = boundingRectange.y;
							resu[2] = i;
							resu[3] = boundingRectange.height;
							resu[4] = boundingRectange.width;
							
							arrays.add(resu);
						}
					}

					@Override
					public void renderImage(ImageRenderInfo arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void endTextBlock() {
						// TODO Auto-generated method stub

					}

					@Override
					public void beginTextBlock() {
						// TODO Auto-generated method stub

					}
				});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrays;
	}

	public static void main(String[] args) {

		List<float[]> ff = getKeyWords("D:/data/zxbTemplet/zxbbuy-1521797199337.pdf");
		for (float[] f : ff) {
			System.out.println("x:" + f[0] + "----y:" + f[1] + "-----" + f[2]);
		}
		System.out.println(sb);

	}
}
