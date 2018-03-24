package org.study.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/**
 * @Title: LocalPdfUtils
 * @Description: java使用itex读取pdf，并搜索关键字，为其盖章
 * @Author: zhaotf
 * @Since:2018年3月24日 下午3:41:04
 * @see {@linkplain https://blog.csdn.net/sdizoea/article/details/75105798}
 */
public class LocalPdfUtils {

	public static void main(String[] args) {

	}

	/**
	 * //根据关键字和pdf路径，全文搜索关键字
	 * 
	 * 查找所有
	 * 
	 * @param fileName
	 *            文件路径
	 * @param keyword
	 *            关键词
	 * @return
	 * @throws Exception
	 */
	public static List matchPage(String fileName, String keyword) throws Exception {
		List items = new ArrayList();
		PdfReader reader = new PdfReader(fileName);
		int pageSize = reader.getNumberOfPages();
		for (int page = 1; page <= pageSize; page++) {
			items.addAll(matchPage(reader, page, keyword));
		}
		return items;
	}

	/**
	 * 根据关键字、文档路径、pdf页数寻找特定的文件内容
	 * 
	 * 在文件中寻找特定的文字内容
	 * 
	 * @param reader
	 * @param pageNumber
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	public static List matchPage(PdfReader reader, Integer pageNumber, String keyword) throws Exception {
		KeyWordPositionListener renderListener = new KeyWordPositionListener();
		renderListener.setKeyword(keyword);
		PdfReaderContentParser parse = new PdfReaderContentParser(reader);
		com.itextpdf.text.Rectangle rectangle = reader.getPageSize(pageNumber);
		renderListener.setPageNumber(pageNumber);
		renderListener.setCurPageSize(rectangle);
		parse.processContent(pageNumber, renderListener);
		return findKeywordItems(renderListener, keyword);
	}

	/**
	 * 找到匹配的关键词块
	 * 
	 * 找到匹配的关键词块
	 * 
	 * @param renderListener
	 * @param keyword
	 * @return
	 */
	public static List findKeywordItems(KeyWordPositionListener renderListener, String keyword) {
		// 先判断本页中是否存在关键词
		List<MatchItem> allItems = renderListener.getAllItems();// 所有块LIST
		StringBuffer sbtemp = new StringBuffer();
		for (MatchItem item : allItems) {// 将一页中所有的块内容连接起来组成一个字符串。
			sbtemp.append(item.getContent());
		}
		if (sbtemp.toString().indexOf(keyword) == -1) {// 一页组成的字符串没有关键词，直接return
			return renderListener.getMatches();
		}
		// 第一种情况：关键词与块内容完全匹配的项
		List matches = renderListener.getMatches();
		// 第二种情况：多个块内容拼成一个关键词，则一个一个来匹配，组装成一个关键词
		sbtemp = new StringBuffer();
		List tempItems = new ArrayList();
		for (MatchItem item : allItems) {
			// 1，关键词中存在某块 2，拼装的连续的块=关键词 3，避开某个块完全匹配关键词
			// 关键词 中国移动 而块为 中 ，国，移动
			// 关键词 中华人民 而块为中，华人民共和国 这种情况解决不了，也不允许存在
			if (keyword.indexOf(item.getContent()) != -1 && !keyword.equals(item.getContent())) {
				tempItems.add(item);
				sbtemp.append(item.getContent());
				if (keyword.indexOf(sbtemp.toString()) == -1) {// 如果暂存的字符串和关键词
																// 不再匹配时
					sbtemp = new StringBuffer(item.getContent());
					tempItems.clear();
					tempItems.add(item);
				}
				if (sbtemp.toString().equalsIgnoreCase(keyword)) {// 暂存的字符串正好匹配到关键词时
					MatchItem tmpitem = getRightItem(tempItems, keyword);
					if (tmpitem != null) {
						matches.add(tmpitem);// 得到匹配的项
					}
					sbtemp = new StringBuffer("");// 清空暂存的字符串
					tempItems.clear();// 清空暂存的LIST
					continue;// 继续查找
				}
			} else {// 如果找不到则清空
				sbtemp = new StringBuffer("");
				tempItems.clear();
			}
		}
		// 第三种情况：关键词存在块中
		for (MatchItem item : allItems) {
			if (item.getContent().indexOf(keyword) != -1 && !keyword.equals(item.getContent())) {
				matches.add(item);
			}
		}
		return matches;
	}

	public static MatchItem getRightItem(List<MatchItem> tempItems, String keyword) {
		for (MatchItem item : tempItems) {

			if (keyword.indexOf(item.getContent()) != -1 && !keyword.equals(item.getContent())) {
				return item;
			}
		}

		return null;
	}
}
