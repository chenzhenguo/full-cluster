package com.hhcf.learn.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hhcf.system.other.State;

/**
 * @Title: UserModel
 * @Description:
 * @Author: zhaotf
 * @Since:2018年6月12日 上午9:33:42
 * @see {@linkplain https://blog.csdn.net/haishu_zheng/article/details/70139120}
 */
public class UserModel implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String state = State.ACTIVE.getState();
	private Set<UserProfileModel> userProfiles = new HashSet<UserProfileModel>();
	private Collection<GrantedAuthority> authorities;// 用户证书是否有效

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<UserProfileModel> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfileModel> userProfiles) {
		this.userProfiles = userProfiles;
	}

}
