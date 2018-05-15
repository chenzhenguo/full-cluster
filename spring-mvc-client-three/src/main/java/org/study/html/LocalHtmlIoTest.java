package org.study.html;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.BaseFont;


/**
 * 
 * @Title: LocalHtmlIoTest
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月28日 下午2:11:28
 */
public class LocalHtmlIoTest {
	private static Logger logger = LoggerFactory.getLogger(LocalHtmlIoTest.class);

	public static void main(String[] args) throws Exception {
////		uploadPactTemplate
//		uploadPactTemplate(null);
////		getPdfAndPath
//		getPdfAndPath(null);
////		getHtml
//		getHtml(null);
		String htmlContent = "<html><body><h3>aaaaaaaaaaaaaaaa</h3></body></html>";
		OutputStream dos = null;
//		String pdfPath = "";
		htmlContent = htmlContent.replace("<meta charset=\"UTF-8\">", "<meta charset=\"UTF-8\"/>");
		try {
			ITextRenderer render = new ITextRenderer();
			// 添加字体,支持中文
			render.getFontResolver().addFont("D:\\data\\zxbTemplet\\SIMSUN.TTC", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			// pdf路径
//			pdfPath = "D:/data/zxbTemplet/zxbbuy-111.pdf";//TODO
			// 设置html模版路径
			render.setDocumentFromString(htmlContent);
			// 设置pdf输出流
//			dos = new DataOutputStream(new ByteArrayOutputStream(htmlContent.getBytes("utf-8").length));
			dos = new BufferedOutputStream(new DataOutputStream(new ByteArrayOutputStream(htmlContent.getBytes("utf-8").length)));
			render.layout();
			render.createPDF(dos);
			render.finishPDF();
//			
//		上传PDF
			byte[] data = "".getBytes();
			System.out.println("eee:"+data.length);
			
//			IOUtils
//			IOUtils.write(data, dos);
			
			
		BufferedInputStream file = new BufferedInputStream(new ByteArrayInputStream(data));
		System.out.println("ttt:"+data.length);
		IOUtils.copy(file, dos);		
		dos.flush();
//		byte[] bdata = IOUtils.toByteArray(file);
		System.out.println("aaa:"+IOUtils.toString(file));
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				dos.close();
			}
		}
		
//		String pdfPath = "pdftest.pdf";
//		生成PDF
//		DataOutputStream dos = new DataOutputStream(new FileOutputStream(pdfPath));
//		BufferedOutputStream dos = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(pdfPath)));
//		dos.flush();
//		dos.close();
//		FileWriter fw = new FileWriter(pdfPath);
//		OutputStreamWriter osw = new FileWriter(pdfPath);

		
		
	}
	
	/**
	 * 生成pdf合同并上传合同文件 TODO
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String uploadPactTemplate(Map<String, String> entity) throws Exception {
		String pdfPath = getPdfAndPath(entity);
		FileInputStream file = new FileInputStream(pdfPath);
		byte[] bdata = IOUtils.toByteArray(file);
//		IOUtils.to

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", entity.get("userId"));// 用户帐号
		params.put("fdata", Base64.encodeBase64(bdata));// 文件数据，base64编码
		params.put("fmd5", DigestUtils.md5Hex(bdata));// 文件md5值
		params.put("ftype", "PDF");// 文件类型
		params.put("fname", "zxbbuy_" + System.currentTimeMillis() + ".PDF");// 文件名 TODO
		params.put("fpages", 3);// 文件页数
		logger.info("电子签章-上传合同文件,参数:" + JSONObject.toJSONString(params));

		String url = "";
		Map<String, Object> rslt = null;
		logger.info("电子签章-上传合同文件结果:" + JSONObject.toJSONString(rslt));
		logger.info("电子签章-上传合同文件,结果:" + entity.get("assetid") + "," + JSON.toJSONString(rslt));
		JSONObject jso = JSON.parseObject((String) rslt.get("responseData"));
		if (!"0".equals(jso.getString("errno"))) {
			logger.warn("电子签章-上传合同文件-失败:" + entity.get("assetid") + "," + entity.get("userId") + ","
					+ JSON.toJSONString(rslt));
			return null;
		}
		String fid = jso.getJSONObject("data").getString("fid");
		if (StringUtils.isBlank(fid)) {
			logger.warn("电子签章-上传合同文件-失败:" + entity.get("assetid") + "," + entity.get("userId") + ","
					+ JSON.toJSONString(rslt));
			return null;
		}
		return fid;
	}

	/**
	 * 根据模版路径生成pdf,并返回pdf路径 TODO
	 */
	public static String getPdfAndPath(Map<String, String> entity) throws Exception {
		String htmlContent = getHtml(entity);
		DataOutputStream dos = null;
		String pdfPath = "";
		htmlContent = htmlContent.replace("<meta charset=\"UTF-8\">", "<meta charset=\"UTF-8\"/>");
		try {
			ITextRenderer render = new ITextRenderer();
			// 添加字体,支持中文
			render.getFontResolver().addFont("BSDigitalSealConst.BS_SIGN_AGRE_TTC_PATH", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			// pdf路径
			pdfPath = "D:/data/zxbTemplet/zxbbuy-" + System.currentTimeMillis() + ".pdf";//TODO
			// 设置html模版路径
			render.setDocumentFromString(htmlContent);
			// 设置pdf输出流
			dos = new DataOutputStream(new ByteArrayOutputStream(htmlContent.getBytes("utf-8").length));
			render.layout();
			render.createPDF(dos);
			render.finishPDF();
			dos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				dos.close();
			}
		}
		return pdfPath;
	}
	
	public static String getHtml(Map<String, String> entity) throws Exception {
		return "aaa";
		// 从文件中加载 HTML 文档
//		File input = new File(BSDigitalSealConst.BS_SIGN_AGRE_HTML_PATH);
//		Document doc = Jsoup.parse(input, "UTF-8");
//		String userId = entity.get("userId");
//		String assetId = entity.get("assetid");
//		String borUserRecId = entity.get("borUserRecId");
//		String productId = null;
//		StringBuffer html = new StringBuffer();
//		// 通过userid和资产列表，获取该出借人合同所需信息
//		List<Map<String, Object>> lenUserInfo = zxbLenUserMgDao.getLenAgreementInfo(userId, assetId);
//		if (!lenUserInfo.isEmpty()) {
//			productId = (String) lenUserInfo.get(0).get("productId");
//
//			html.append("<tr height=\"25\">" + " <td>" + lenUserInfo.get(0).get("mobileno") + "</td>" + " <td>"
//					+ lenUserInfo.get(0).get("cust_nm") + "</td> " + " <td>" + lenUserInfo.get(0).get("certif_id")
//					+ "</td>   " + " <td>￥" + lenUserInfo.get(0).get("user_bid_money") + "</td> " + " <td>"
//					+ lenUserInfo.get(0).get("num_periods") + "个月</td> " + " <td>￥" + lenUserInfo.get(0).get("money")
//					+ "</td>" + "</tr>");
//		}
//		// 通过userid和项目编码，获取该项目所有出借人（除该userid）合同所需信息
//		List<Map<String, Object>> lenUsersInfo = zxbLenUserMgDao.findLenAgreementInfo(productId, userId);
//		for (Map<String, Object> lenUsersMap : lenUsersInfo) {
//			html.append("<tr height=\"25\">" + " <td>" + lenUsersMap.get("mobileno") + "</td>" + " <td>"
//					+ lenUsersMap.get("cust_nm") + "</td> " + " <td>" + lenUsersMap.get("certif_id") + "</td>   "
//					+ " <td>￥" + lenUsersMap.get("user_bid_money") + "</td> " + " <td>" + lenUsersMap.get("num_periods")
//					+ "个月</td> " + " <td>￥" + lenUsersMap.get("money") + "</td>" + "</tr>");
//		}
//
//		// 获取该项目借款人信息
//		List<Map<String, Object>> borUserInfo = zxbBorUserRecDao.getBorAgreementInfo(assetId);
//		StringBuffer borUser = new StringBuffer();
//		borUser.append("<tr height=\"50\">" + " <td >借款用途：" + "</td>" + " <td  >" + borUserInfo.get(0).get("purpose")
//				+ " </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >借款本金数额：" + "</td>" + " <td  >" + "￥"
//				+ borUserInfo.get(0).get("user_bid_money") + " </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >借款年利率：" + "</td>" + " <td  >"
//				+ borUserInfo.get(0).get("normalrate") + " %</tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >起息日：" + "</td>" + " <td  >"
//				+ borUserInfo.get(0).get("transfer_pay_date") + " </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >借款期限：" + "</td>" + " <td  >"
//				+ borUserInfo.get(0).get("num_periods") + " 个月 </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >还款方式：" + "</td>" + " <td  >"
//				+ HFDepositConst.repayTypeMap.get(borUserInfo.get(0).get("repay_type")) + " </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >月偿还款本息数额：" + "</td>" + " <td  >" + "￥"
//				+ borUserInfo.get(0).get("money") + " </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >还款分期月数：" + "</td>" + " <td  >"
//				+ borUserInfo.get(0).get("num_periods") + " 个月 </tr>");
//		borUser.append("<tr height=\"50\">" + " <td  >还款日：" + "</td>" + " <td  >" + "每月 "
//				+ borUserInfo.get(0).get("repayplandate") + " 日" + " </tr>");
//		// 添加至html模板
//		doc.getElementById("today_date").after(DateUtils.date2Str(new Date(), DateUtils.PATTERN[13]));
//		doc.getElementById("bor_title").after(html.toString());// 元素后添加
//		ZxbBorUserRecEntity zxbBorUserRecEntity = zxbBorUserRecDao.getZxbBorUserMgById(borUserRecId);
//		doc.getElementById("bor_nm").after("姓名：" + ZxbUtil.setValueDsp("4", zxbBorUserRecEntity.getBorNm()));// 借款人姓名
//		doc.getElementById("bor_mobileno")
//				.after("借款人手机号：" + ZxbUtil.setValueDsp("1", zxbBorUserRecEntity.getBorLoginId()));// 借款人手机号
//		doc.getElementById("bor_id").after("身份证号：" + ZxbUtil.setValueDsp("2", zxbBorUserRecEntity.getCertifId()));// 借款人身份证号
//		if (StringUtils.isNotBlank(entity.get("loanMerchantRecId"))) {
//			ZxbLoanBusinessRecEntity zxbLoanBusinessRecEntity = zxbLoanBusinessRecDao
//					.getZxbLoanBusinessRecById(entity.get("loanMerchantRecId"));
//			doc.getElementById("bor_business").after(zxbLoanBusinessRecEntity.getBusinessLoginId());
//		}
//		doc.getElementById("product_info").after(borUser.toString());
//		// 修改后html输出
//		String fileName = "zxb-hh-" + System.currentTimeMillis() + ".html";//TODO
//		// 输出字符集为UTF-8
//		BufferedWriter writer = new BufferedWriter(
//				new OutputStreamWriter(new FileOutputStream("D:/data/zxbTemplet/" + fileName, true), "UTF-8"));// TODO
//		writer.write(doc.html());
//		writer.close();
//		return doc.html();
	}
}
