package com.hhcf.learn.model;

/**
 * @Title: UserProfileType
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月11日 下午4:57:41
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-mvc-4-and-spring-security-4-integration-example.html}
 */
public enum UserProfileType {
	USER("USER"), DBA("DBA"), ADMIN("ADMIN");

	String userProfileType;

	private UserProfileType(String userProfileType) {
		this.userProfileType = userProfileType;
	}

	public String getUserProfileType() {
		return userProfileType;
	}
}
