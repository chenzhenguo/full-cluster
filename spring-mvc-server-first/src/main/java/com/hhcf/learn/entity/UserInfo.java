package com.hhcf.learn.entity;

import java.io.Serializable;

/**
 * 
 * @Title: UserInfo.java
 * @Description: TODO
 * @author zhaotf
 * @date 2017年12月31日 下午2:14:13
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = -4518301899608334776L;
	private Long id;
	private String userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
