package com.hhcf.backend.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @Title: UserModel.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午3:29:35
 */
public class UserModel implements Serializable {
	private static final long serialVersionUID = -4704221318758733552L;
	private Long id;
	private String name;
	private int age;
	private List<UserModel> friends;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<UserModel> getFriends() {
		return friends;
	}

	public void setFriends(List<UserModel> friends) {
		this.friends = friends;
	}

}
