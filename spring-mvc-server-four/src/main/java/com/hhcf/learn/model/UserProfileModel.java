package com.hhcf.learn.model;

import java.io.Serializable;

import com.hhcf.system.other.UserProfileType;

/**
 * @Title: UserProfileModel
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月13日 上午8:34:14
 * @see {@linkplain https://www.yiibai.com/spring-security/spring-security-4-hibernate-annotation-example.html}
 */
public class UserProfileModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String type = UserProfileType.USER.getUserProfileType();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserProfileModel))
			return false;
		UserProfileModel other = (UserProfileModel) obj;
		if (id != other.id)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ",  type=" + type + "]";
	}

}
