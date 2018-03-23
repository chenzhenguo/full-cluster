package org.study.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Title: LocalHtmlUtils
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月23日 下午2:19:54
 * @see {@linkplain https://blog.csdn.net/chenleixing/article/details/47121679}
 * @see {@linkplain https://www.cnblogs.com/shihuc/p/5515766.html}
 */
public class LocalHtmlUtils {

	public static void main(String[] args) throws Exception {
		method2();
	}

	/**
	 * 使用Jsoup解析和操作HTML
	 * 
	 */
	public static void method2() throws Exception {
		// // 直接从字符串中输入 HTML 文档
		// String html = "<html><head><title>开源中国社区</title></head>" +
		// "<body><p>这里是 jsoup 项目的相关文章</p></body></html>";
		// Document doc = Jsoup.parse(html);

		// // 从URL直接加载 HTML 文档
		// Document doc = Jsoup.connect("http://www.oschina.net/").get();
		// String title = doc.title();
		// Document doc = Jsoup.connect("http://www.oschina.net/").data("query",
		// "Java") // 请求参数
		// .userAgent("I’m jsoup") // 设置User-Agent
		// .cookie("auth", "token") // 设置cookie
		// .timeout(3000) // 设置连接超时时间
		// .post(); // 使用POST方法访问URL

		// 从文件中加载 HTML 文档
		File input = new File("D:/data/zxbTemplet/zxb-hh.html");
		// Document doc = Jsoup.parse(input, "UTF-8",
		// "http://www.oschina.net/");
		Document doc = Jsoup.parse(input, "UTF-8");

		String html = "<tr height=\"25\">" + " <td>1533333324</td>        " + " <td>测试者</td>                "
				+ " <td>14****aaaaaaa*****24</td>   " + " <td>￥111120.00</td>           "
				+ " <td>16个月</td>              " + " <td>￥2ffwef6.50</td>" + "</tr>";
		// System.out.println(doc.html());
		// doc.getElementById("bor_title").append(html);//内部添加
		doc.getElementById("bor_title").after(html);// 元素后添加
		System.out.println(doc.html());

		File outFile = new File("D:/data/zxbTemplet/zxb-hh-" + System.currentTimeMillis() + ".html");
		FileOutputStream fos = new FileOutputStream(outFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		osw.write(doc.html());
		osw.close();
	}

	public static void method3() throws IOException {
		File input = new File("./example.html");
		Document doc = Jsoup.parse(input, "UTF-8");
		System.out.println(doc.html());
		Elements content = doc.getElementsByTag("body");
		// 找到body的内容
		String body = content.get(0).html();
		System.out.println(body);

		// 读取控制页面右键的代码片段
		File pice = new File("./pice.html");
		Document pdoc = Jsoup.parse(pice, "UTF-8");
		Element control_area = pdoc.getElementById("tk-cms-page-context-menu-control-area");

		// 将业务相关的html代码片添加到右键菜单控制区域中
		control_area.append(body);

		// 将带有业务相关代码的html内容填写到原来的example.html文件的body区域
		content.get(0).html(pdoc.html());

		FileOutputStream fos = new FileOutputStream("./output.html", false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		osw.write(doc.html());
		osw.close();
	}

	public static String method1() {
		// 用于存储html字符串
		StringBuilder stringHtml = new StringBuilder();
		PrintStream printStream = null;
		try {
			// 打开文件
			printStream = new PrintStream(new FileOutputStream("/data/zxbTemplet/test.html"));
			// 输入HTML文件内容
			stringHtml.append("<html><head>");
			stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">");
			stringHtml.append("<title>测试报告文档</title>");
			stringHtml.append("</head>");
			stringHtml.append("<body>");
			stringHtml.append("<div>hello</div>");
			stringHtml.append("</body></html>");
			// 将HTML文件内容写入文件中
			printStream.println(stringHtml.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (printStream != null) {
				printStream.close();
			}
		}
		return null;
	}
}
