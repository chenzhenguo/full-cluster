package com.hhcf.system.other;

/**
 * @Title: State
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 上午8:37:07
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-hibernate-annotation-example.html}
 */
public enum State {
	ACTIVE("Active"), INACTIVE("Inactive"), DELETED("Deleted"), LOCKED("Locked");

	private String state;

	private State(final String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	@Override
	public String toString() {
		return this.state;
	}

	public String getName() {
		return this.name();
	}
}
