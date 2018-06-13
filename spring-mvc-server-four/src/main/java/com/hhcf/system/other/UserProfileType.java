package com.hhcf.system.other;

/**
 * @Title: UserProfileType
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 上午8:35:56
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-hibernate-annotation-example.html}
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
