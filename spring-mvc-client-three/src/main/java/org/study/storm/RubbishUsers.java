package org.study.storm;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @Title: RubbishUsers
 * @Description:
 * @Author: zhaotf
 * @Since:2018年3月9日 下午2:02:23
 * @see {@linkplain https://www.cnblogs.com/jietang/p/5423438.html}
 */
public class RubbishUsers implements Serializable {
	private static final long serialVersionUID = 1L;
	// 用户归属地市编码
	private Integer homeCity;
	// 用户编码
	private Integer userId;
	// 用户号码
	private Integer msisdn;
	// 短信内容
	String smsContent;

	public final static String HOMECITY_COLUMNNAME = "home_city";
	public final static String USERID_COLUMNNAME = "user_id";
	public final static String MSISDN_COLUMNNAME = "msisdn";
	public final static String SMSCONTENT_COLUMNNAME = "sms_content";

	public final static Integer[] SENSITIVE_HOMECITYS = new Integer[] { 591/* 福州 */, 592 /* 厦门 */ };

	// 敏感关键字,后续可以考虑单独开辟放入缓存或数据库中,这里仅仅为了Demo演示
	public final static String SENSITIVE_KEYWORD1 = "Bad";
	public final static String SENSITIVE_KEYWORD2 = "racketeer";
	public final static String[] SENSITIVE_KEYWORDS = new String[] { SENSITIVE_KEYWORD1, SENSITIVE_KEYWORD2 };

	public Integer getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(Integer homeCity) {
		this.homeCity = homeCity;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(Integer msisdn) {
		this.msisdn = msisdn;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("homeCity", homeCity)
				.append("userId", userId).append("msisdn", msisdn).append("smsContent", smsContent).toString();
	}
}
