package com.hhcf.learn.model;

/**
 * 
 * @Title: UserModel
 * @Description:用户实体
 * @Author: zhaotf
 * @Since:2017年7月11日 上午11:22:50
 * @Version:1.0
 */
@SuppressWarnings("serial")
public class UserModel implements java.io.Serializable {
	private String id;// 用户名
	private String userType;
	private String trueName;
	private String roleNames;
	private String roleCodes;
	private String browser;// 用户使用浏览器类型
	private String userKey;// 用户验证唯一标示
	private String password;// 用户密码
	private Short activitiSync;// 是否同步工作流引擎
	private Short status;// 状态1：在线,2：离线,0：禁用,9:删除
	private byte[] signature;// 签名文件

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Short getActivitiSync() {
		return activitiSync;
	}

	public void setActivitiSync(Short activitiSync) {
		this.activitiSync = activitiSync;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

}
