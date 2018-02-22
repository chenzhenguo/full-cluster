package com.hhcf.backend.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @Title: SimpleModel.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月22日 下午4:20:13
 */
public class SimpleModel implements Serializable {
	private static final long serialVersionUID = 1115248640738618097L;
	private String name;
	private int age;
//	private Map<String, Integer> map;

	public SimpleModel() {
	}

	public SimpleModel(String name, int age, Map<String, Integer> map) {
		this.name = name;
		this.age = age;
//		this.map = map;
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

//	public Map<String, Integer> getMap() {
//		return map;
//	}
//
//	public void setMap(Map<String, Integer> map) {
//		this.map = map;
//	}

}
